package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Exceptions.PostNotFoundException;
import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.Notification;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.NotificationRepository;
import com.connectify.demo.Repository.PostRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import com.connectify.demo.service.PostService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserInfoRepository userInfoRepository;
    private final NotificationService notificationService;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post addPost(Post post, Long userId) {

        log.info("Creating post for userId: {}", userId);

        if (!userInfoRepository.existsById(userId)) {
            log.error("Post creation failed. User not found with id: {}", userId);

            throw new UserNotFoundException(
                    "user is not found by Id - " + userId,
                    "user is not found!! check it once"
            );
        }

        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        UserInfo user = userInfo.get();

        user.setNoOfPost(user.getPosts().size() + 1);

        post.setUser(user);
        post.setTime(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        log.info(
                "Post created successfully. PostId: {}, UserId: {}",
                savedPost.getPostId(),
                userId
        );

        List<Followers> followers = user.getFollowers();

        log.debug(
                "Generating notifications for {} followers",
                followers.size()
        );

        for (Followers follower : followers) {

            notificationService.sendNotification(
                    follower.getFrom(),
                    "post is uploaded by user " + user.getName(),
                    userId,
                    savedPost
            );
        }

        log.info(
                "Notifications processed successfully for postId: {}",
                savedPost.getPostId()
        );

        return savedPost;
    }

    @Override
    @Transactional
    public Page<Post> getAllPost(int page, int size) {

        log.info(
                "Fetching posts with pagination. Page: {}, Size: {}",
                page,
                size
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "time")
        );

        Page<Post> posts = postRepository.findAll(pageable);

        log.info(
                "Fetched {} posts out of {} total posts",
                posts.getNumberOfElements(),
                posts.getTotalElements()
        );

        return posts;
    }
    @Override
    public Post getPostById(Long postId) {

        log.debug("Fetching post with id: {}", postId);

        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {

            log.error("Post not found with id: {}", postId);

            throw new PostNotFoundException(
                    "post is not found by Id - " + postId,
                    "Wrong postId check the postId once"
            );
        }

        return post.get();
    }

    @Override
    public String deletePostById(Long postId) {

        log.info("Delete request received for postId: {}", postId);

        if (!postRepository.existsById(postId)) {

            log.error("Delete failed. Post not found with id: {}", postId);

            throw new PostNotFoundException(
                    "post is not found with Id- " + postId,
                    "Wrong postId check the postId once"
            );
        }

        postRepository.deleteById(postId);

        log.info("Post deleted successfully. PostId: {}", postId);

        return "post is deleted successfully";
    }

    @Override
    @Transactional
    public int removePostByUserId(Long userId, Long postId) {

        log.info(
                "Removing postId: {} by userId: {}",
                postId,
                userId
        );

        Post post = entityManager.find(Post.class, postId);

        if (post == null || !post.getUser().getId().equals(userId)) {

            log.warn(
                    "Post removal failed. Invalid ownership or post not found. UserId: {}, PostId: {}",
                    userId,
                    postId
            );

            return 0;
        }

        int deletedNotifications = entityManager.createQuery(
                        "DELETE FROM Notification n WHERE n.post.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();

        log.debug(
                "Deleted {} notifications related to postId: {}",
                deletedNotifications,
                postId
        );

        Optional<UserInfo> userOpt = userInfoRepository.findById(userId);

        if (userOpt.isPresent()) {

            UserInfo userInfo = userOpt.get();

            int newCount = userInfo.getNoOfPost() > 0
                    ? userInfo.getNoOfPost() - 1
                    : 0;

            userInfo.setNoOfPost(newCount);

            userInfoRepository.save(userInfo);
        }

        entityManager.remove(post);

        log.info(
                "Post removed successfully. UserId: {}, PostId: {}",
                userId,
                postId
        );

        return 1;
    }

    @Override
    public List<Post> allPostByUserId(Long userId) {

        log.info("Fetching posts for userId: {}", userId);

        Optional<UserInfo> userInfo =
                userInfoRepository.findById(userId);

        if (userInfo.isEmpty()) {

            log.error("User not found with id: {}", userId);

            throw new UserNotFoundException(
                    "user is not found with Id - " + userId,
                    "user is not found!! check it once"
            );
        }

        return userInfo.get().getPosts();
    }

    @Override
    public Post updatePost(Post post, Long postId) {

        log.info("Updating post with id: {}", postId);

        Post existingPost = getPostById(postId);

        if (post.getTopic() != null) {
            existingPost.setTopic(post.getTopic());
        }

        if (post.getPostDescription() != null) {
            existingPost.setPostDescription(post.getPostDescription());
        }

        if (post.getPostUrl() != null) {
            existingPost.setPostUrl(post.getPostUrl());
        }

        postRepository.save(existingPost);

        log.info("Post updated successfully. PostId: {}", postId);

        return existingPost;
    }
}
