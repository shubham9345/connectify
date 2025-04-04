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

        if(!userInfoRepository.existsById(userId)){
            throw new RuntimeException("user is not found by Id - " + userId);
        }
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        post.setUser(userInfo.get());
        post.setTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPost() {
        return postRepository.findAll();
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
        String jpql = "DELETE FROM Post p WHERE p.user.id = :userId AND p.postId = :postId";
        return entityManager.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .executeUpdate();
        }
    }
