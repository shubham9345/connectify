package com.connectify.demo.Service;

import com.connectify.demo.Exceptions.PostNotFoundException;
import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.Notification;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.NotificationRepository;
import com.connectify.demo.Repository.PostRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Post addPost(Post post, Long userId) {

        if (!userInfoRepository.existsById(userId)) {
            throw new UserNotFoundException("user is not found by Id - " + userId, "user is not found!! check it once");
        }
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        UserInfo user = userInfo.get();
        user.setNoOfPost(user.getPosts().size() + 1);
        post.setUser(user);
        post.setTime(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        List<Followers> followers = user.getFollowers();
        for (int i = 0; i < followers.size(); i++) {
            List<Notification> notf = followers.get(i).getFrom().getNotifications();
            if (notf.isEmpty()) {
                List<Notification> notfi = new ArrayList<>();
                Notification notification = new Notification();
                notification.setMessage("post is uploaded by user " + user.getName());
                notification.setUser(followers.get(i).getFrom());
                notification.setTime(LocalDateTime.now());
                notification.setByUserId(userId);
                notification.setPost(savedPost);
                notfi.add(notification);
                followers.get(i).getFrom().setNotifications(notfi);
            } else {
                Notification notification = new Notification();
                notification.setMessage("post is uploaded by user " + user.getName());
                notification.setUser(followers.get(i).getFrom());
                notification.setTime(LocalDateTime.now());
                notification.setByUserId(userId);
                notification.setPost(savedPost);
                notf.add(notification);
                followers.get(i).getFrom().setNotifications(notf);
            }
            userInfoRepository.save(followers.get(i).getFrom());
        }

        return savedPost;
    }

    public List<Post> getAllPost() {
        List<Post> allPost = postRepository.findAll();
        Collections.reverse(allPost);
        return allPost;
    }

    public Post getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("post is not found by Id - " + postId, "Wrong postId check the postId once");
        }
        return post.get();
    }

    public String deletePostById(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("post is not found with Id- " + postId, "Wrong postId check the postId once");
        }
        postRepository.deleteById(postId);
        return "post is deleted successfully";
    }

    public int removePostByUserId(Long userId, Long postId) {
        Optional<UserInfo> user = userInfoRepository.findById(userId);
        UserInfo userInfo = user.get();
        userInfo.setNoOfPost(userInfo.getPosts().size() - 1);
        userInfoRepository.save(userInfo);
        String jpql = "DELETE FROM Post p WHERE p.user.id = :userId AND p.postId = :postId";
        return entityManager.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .executeUpdate();
    }

    public List<Post> allPostByUserId(Long userId) {
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if (userInfo.isEmpty()) {
            throw new UserNotFoundException("user is not found with Id - " + userId, "user is not found!! check it once");
        }
        UserInfo user = userInfo.get();
        return user.getPosts();
    }

    public Post updatePost(Post post, Long postId) {
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
        return existingPost;
    }
}
