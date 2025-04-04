package com.connectify.demo.Service;

import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.PostRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @PersistenceContext
    private EntityManager entityManager;

    public Post addPost(Post post, Long userId) {

        if (!userInfoRepository.existsById(userId)) {
            throw new RuntimeException("user is not found by Id - " + userId);
        }
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        UserInfo user = userInfo.get();
        user.setNoOfPost(user.getPosts().size() + 1);
        post.setUser(user);
        post.setTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPost() {
        List<Post> allPost = postRepository.findAll();
        Collections.reverse(allPost);
        return allPost;
    }

    public Post getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new RuntimeException("post is not found by Id - " + postId);
        }
        return post.get();
    }

    public String deletePostById(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("post is not found with Id- " + postId);
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
            throw new RuntimeException("user is not found with Id - " + userId);
        }
        UserInfo user = userInfo.get();
        return user.getPosts();
    }
    public Post updatePost (Post post, Long postId){
        Post existingPost = getPostById(postId);
        if(post.getTopic()!=null){
            existingPost.setTopic(post.getTopic());
        }
        if(post.getPostDescription()!=null){
            existingPost.setPostDescription(post.getPostDescription());
        }
        if(post.getPostUrl()!=null){
            existingPost.setPostUrl(post.getPostUrl());
        }
        postRepository.save(existingPost);
        return existingPost;
    }
}
