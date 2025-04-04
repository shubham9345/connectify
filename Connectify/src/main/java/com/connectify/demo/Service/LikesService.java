package com.connectify.demo.Service;

import com.connectify.demo.Model.Likes;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.LikesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class LikesService {
    @Autowired
    private LikesRepository likeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PostService postService;

    public Likes addLike(Long userId, Long postId) {
        UserInfo user = userInfoService.getUserbyId(userId);
        if (user == null) {
            throw new RuntimeException("User is not found with Id.");
        }
        Post post = postService.getPostById(postId);
        post.setNoOfLikes(post.getLikes().size()+1);

        if (post == null) {
            throw new RuntimeException("post not found this id");
        }

        Likes likes = new Likes();
        likes.setPosts(post);
        likes.setUser(user);
        likes.setTime(LocalDateTime.now());
        likeRepository.save(likes);
        return likes;
    }

    public List<Likes> allLikesByPostId(Long postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            throw new RuntimeException("post not found this id");
        }
        List<Likes> allLikes = post.getLikes();
        Collections.reverse(allLikes);
        return allLikes;
    }

    public String deleteLikesById(Long LikesId) {
        if (!likeRepository.existsById(LikesId)) {
            throw new RuntimeException("Likes is not found with id" + LikesId);
        }
        likeRepository.deleteById(LikesId);
        return "Likes is deleted with Id - " + LikesId;
    }

    public int removeLikeByUserId(Long userId, Long postId) {
        Post post = postService.getPostById(postId);
        post.setNoOfLikes(post.getLikes().size()-1);
        String jpql = "DELETE FROM Likes l WHERE l.user.id = :userId AND l.posts.id = :postId";
        return entityManager.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .executeUpdate();
    }
}
