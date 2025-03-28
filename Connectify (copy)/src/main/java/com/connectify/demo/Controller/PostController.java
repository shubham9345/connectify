package com.connectify.demo.Controller;

import com.connectify.demo.Model.Post;
import com.connectify.demo.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/add-post")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post newPost = postService.addPost(post);
        return new ResponseEntity<>(newPost, HttpStatus.OK);
    }

    @GetMapping("/all-post")
    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> allPost = postService.getAllPost();
        return new ResponseEntity<>(allPost, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Post Post = postService.getPostById(postId);
        return new ResponseEntity<>(Post, HttpStatus.OK);
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable Long postId) {
        try {
            postService.deletePostById(postId);
            return ResponseEntity.ok("Post deleted successfully with Id- " + postId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
