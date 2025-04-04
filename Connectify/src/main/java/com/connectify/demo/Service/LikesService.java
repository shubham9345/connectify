package com.connectify.demo.Service;

import com.connectify.demo.Model.*;
import com.connectify.demo.Repository.LikesRepository;
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
    private UserInfoRepository userInfoRepository;
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
        List<Followers> followers = user.getFollowers();
        for(int i = 0; i<followers.size(); i++){
            List<Notification> notf  = followers.get(i).getFrom().getNotifications();
            if(notf.isEmpty()){
                List<Notification> notfi = new ArrayList<>();
                Notification notification = new Notification();
                notification.setMessage("post with postId  " + post.getPostId() + " is Liked by User " + user.getName());
                notification.setUser(followers.get(i).getFrom());
                notification.setTime(LocalDateTime.now());
                notfi.add(notification);
                followers.get(i).getFrom().setNotifications(notfi);
            }else{
                Notification notification = new Notification();
                notification.setMessage("post with postId  " + post.getPostId() + " is Liked by User " + user.getName());
                notification.setUser(followers.get(i).getFrom());
                notification.setTime(LocalDateTime.now());
                notf.add(notification);
                followers.get(i).getFrom().setNotifications(notf);
            }
            userInfoRepository.save(followers.get(i).getFrom());
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
