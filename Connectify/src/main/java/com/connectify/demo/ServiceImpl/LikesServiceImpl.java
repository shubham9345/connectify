package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Dto.LikeResponse;
import com.connectify.demo.Exceptions.PostNotFoundException;
import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.*;
import com.connectify.demo.Repository.LikesRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import com.connectify.demo.service.LikesService;
import com.connectify.demo.service.PostService;
import com.connectify.demo.service.UserInfoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likeRepository;
    private final UserInfoService userInfoService;
    private final PostService postService;
    private final NotificationService notificationService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Likes addLike(Long userId, Long postId) {

        log.info(
                "Like request received. UserId: {}, PostId: {}",
                userId,
                postId
        );

        UserInfo user = userInfoService.getUserbyId(userId);

        Post post = postService.getPostById(postId);

        if (likeRepository.existsByPostsAndUser(post, user)) {

            log.warn(
                    "Like already exists. UserId: {}, PostId: {}",
                    userId,
                    postId
            );

            throw new UserNotFoundException(
                    "post is already Liked By user",
                    "post is already liked by user"
            );
        }

        post.setNoOfLikes(post.getNoOfLikes() + 1);

        List<Followers> followers = user.getFollowers();

        log.debug(
                "Generating notifications for {} followers",
                followers.size()
        );

        for (Followers follower : followers) {

            notificationService.sendNotification(
                    follower.getFrom(),
                    "post with postId "
                            + post.getPostId()
                            + " is Liked by User "
                            + user.getUsername(),
                    userId,
                    post
            );
        }

        Likes likes = new Likes();
        likes.setPosts(post);
        likes.setUser(user);
        likes.setTime(LocalDateTime.now());

        likeRepository.save(likes);

        log.info(
                "Like added successfully. LikeId: {}, UserId: {}, PostId: {}",
                likes.getLikeId(),
                userId,
                postId
        );

        return likes;
    }

    @Override
    @Transactional
    public Page<LikeResponse> allLikesByPostId(
            Long postId,
            int page,
            int size
    ) {

        log.info(
                "Fetching likes for postId: {}, page: {}, size: {}",
                postId,
                page,
                size
        );

        postService.getPostById(postId);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "time")
        );

        Page<LikeResponse> likesPage =
                likeRepository
                        .findByPosts_PostId(postId, pageable)
                        .map(like -> new LikeResponse(
                                like.getLikeId(),
                                like.getUser().getUsername(),
                                like.getTime()
                        ));

        log.info(
                "Fetched {} likes out of {} total likes for postId: {}",
                likesPage.getNumberOfElements(),
                likesPage.getTotalElements(),
                postId
        );

        return likesPage;
    }

    @Override
    public String deleteLikesById(Long likesId) {

        log.info(
                "Delete like request received. LikeId: {}",
                likesId
        );

        if (!likeRepository.existsById(likesId)) {

            log.error(
                    "Like not found. LikeId: {}",
                    likesId
            );

            throw new RuntimeException(
                    "Likes is not found with id " + likesId
            );
        }

        likeRepository.deleteById(likesId);

        log.info(
                "Like deleted successfully. LikeId: {}",
                likesId
        );

        return "Likes is deleted with Id - " + likesId;
    }

    @Override
    public int removeLikeByUserId(Long userId, Long postId) {

        log.info(
                "Removing like. UserId: {}, PostId: {}",
                userId,
                postId
        );

        Post post = postService.getPostById(postId);

        post.setNoOfLikes(
                post.getNoOfLikes()- 1
        );

        String jpql =
                "DELETE FROM Likes l " +
                        "WHERE l.user.id = :userId " +
                        "AND l.posts.id = :postId";

        int deleted = entityManager.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .executeUpdate();

        log.info(
                "Like removal completed. Deleted rows: {}",
                deleted
        );

        return deleted;
    }
}