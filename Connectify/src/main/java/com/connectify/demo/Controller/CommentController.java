package com.connectify.demo.Controller;

import com.connectify.demo.Dto.CommentResponse;
import com.connectify.demo.Model.Comment;
import com.connectify.demo.ServiceImpl.CommentServiceImpl;
import com.connectify.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add-comment/{userId}/{postId}")
    public ResponseEntity<Comment> addComment(@RequestBody String message, @PathVariable Long userId, @PathVariable Long postId) {
        Comment comments = commentService.addComments(message, userId, postId);
        return new ResponseEntity<Comment>(comments, HttpStatus.OK);
    }

    @GetMapping("/all-comments/{postId}")
    public ResponseEntity<Page<CommentResponse>> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                commentService.allCommentsByPostId(
                        postId,
                        page,
                        size
                )
        );
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId) {
        String message = commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}/{postId}/{commentId}")
    public String removeCommentByUserId(@PathVariable Long userId, @PathVariable Long postId, @PathVariable Long commentId) {
        int ans = commentService.removeCommentByUserId(userId, postId, commentId);
        if (ans == 1) {
            return "Remove comment on post with Id - " + postId;
        } else {
            return "something wrong in remove comment on post with Id - " + postId;
        }
    }
}
