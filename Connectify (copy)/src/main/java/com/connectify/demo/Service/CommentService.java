package com.connectify.demo.Service;

import com.connectify.demo.Model.Comment;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PostService postService;

    public Comment addComments(String message, Long userId, Long postId) {
        UserInfo user = userInfoService.getUserbyId(userId);
        if (user == null) {
            throw new RuntimeException("User is not found with Id.");
        }
        Post post = postService.getPostById(postId);

        if (post == null) {
            throw new RuntimeException("post not found this id");
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
        return post.getComments();
    }

    public String deleteCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("comment is not found with id" + commentId);
        }
        commentRepository.deleteById(commentId);
        return "comment is deleted with Id - " + commentId;
    }
}

