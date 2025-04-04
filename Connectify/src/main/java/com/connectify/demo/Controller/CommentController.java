package com.connectify.demo.Controller;

import com.connectify.demo.Model.Comment;
import com.connectify.demo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/add-comment/{userId}/{postId}")
    public ResponseEntity<Comment> addComment(@RequestBody String message, @PathVariable Long userId, @PathVariable Long postId) {
        Comment comments = commentService.addComments(message, userId, postId);
        return new ResponseEntity<Comment>(comments, HttpStatus.OK);
    }

    @GetMapping("/all-comments/{postId}")
    public ResponseEntity<List<Comment>> allCommentsByPostId(@PathVariable Long postId) {
        List<Comment> allComments = commentService.allCommentsByPostId(postId);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId) {
        String message = commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{userId}/{postId}")
    public int removeCommentByUserId(@PathVariable Long userId , @PathVariable Long postId){
        return  commentService.removeCommentByUserId(userId,postId);
    }
}
