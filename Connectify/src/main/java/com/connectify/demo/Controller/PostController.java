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
@CrossOrigin
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/add-post/{userId}")
    public ResponseEntity<Post> addPost(@RequestBody Post post, @PathVariable Long userId) {
        Post newPost = postService.addPost(post,userId);
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

    @DeleteMapping("/delete-post/{userId}/{postId}")
    public String removePostByUserId(@PathVariable Long userId , @PathVariable Long postId){
       int ans = postService.removePostByUserId(userId,postId);
       if(ans == 1){
           return "post is deleted with Id - " + postId;
       }else{
           return "post is not deleted with Id - " + postId;
       }

    }
    @GetMapping("/all-post/{userId}")
    public ResponseEntity<?> getPostByUserId(@PathVariable Long userId){
        List<Post> allPost = postService.allPostByUserId(userId);
        return new ResponseEntity<>(allPost,HttpStatus.OK);
    }
}
