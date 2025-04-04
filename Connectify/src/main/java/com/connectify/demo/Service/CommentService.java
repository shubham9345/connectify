package com.connectify.demo.Service;

import com.connectify.demo.Model.*;
import com.connectify.demo.Repository.CommentRepository;
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
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PostService postService;

    public Comment addComments(String message, Long userId, Long postId) {
        UserInfo user = userInfoService.getUserbyId(userId);
        if (user == null) {
            throw new RuntimeException("User is not found with Id.");
        }
        Post post = postService.getPostById(postId);
        post.setNoOfComments(post.getComments().size()+1);
//        UserInfo userInfo = post.getUser();
//        if(userInfo.getId()==userId){
//            throw new RuntimeException("")
//        }
        if (post == null) {
            throw new RuntimeException("post not found this id");
        }
        List<Followers> followers = user.getFollowers();
        for(int i = 0; i<followers.size(); i++){
            List<Notification> notf  = followers.get(i).getFrom().getNotifications();
            if(notf.isEmpty()){
                List<Notification> notfi = new ArrayList<>();
                Notification notification = new Notification();
                notification.setMessage("post with postId " + post.getPostId() +" is commented " +   "by user " + user.getName());
                notification.setUser(followers.get(i).getFrom());
                notification.setTime(LocalDateTime.now());
                notfi.add(notification);
                followers.get(i).getFrom().setNotifications(notfi);
            }else{
                Notification notification = new Notification();
                notification.setMessage("post with postId " + post.getPostId() +" is commented " +   "by user " + user.getName());
                notification.setUser(followers.get(i).getFrom());
                notification.setTime(LocalDateTime.now());
                notf.add(notification);
                followers.get(i).getFrom().setNotifications(notf);
            }
            userInfoRepository.save(followers.get(i).getFrom());
        }

        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setUser(user);
        comment.setPost(post);
        comment.setTime(LocalDateTime.now());
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> allCommentsByPostId(Long postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            throw new RuntimeException("post not found this id");
        }
        List<Comment> allComments = post.getComments();
        Collections.reverse(allComments);
        return allComments;
    }

    public String deleteCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("comment is not found with id" + commentId);
        }
        commentRepository.deleteById(commentId);
        return "comment is deleted with Id - " + commentId;
    }

    public int removeCommentByUserId(Long userId, Long postId, Long commentId) {
        Post post = postService.getPostById(postId);
        post.setNoOfComments(post.getComments().size()-1);
        String jpql = "DELETE FROM Comment c WHERE c.user.id = :userId AND c.post.id = :postId AND c.commentId = :commentId";
        return entityManager.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .setParameter("commentId", commentId)
                .executeUpdate();
    }
}

