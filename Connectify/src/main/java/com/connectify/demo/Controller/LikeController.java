package com.connectify.demo.Controller;

import com.connectify.demo.Model.Comment;
import com.connectify.demo.Model.Likes;
import com.connectify.demo.Service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikesService likeService;

    @PostMapping("/add-like/{userId}/{postId}")
    public ResponseEntity<Likes> addComment(@PathVariable Long userId, @PathVariable Long postId) {
         Likes likes = likeService.addLike( userId, postId);
        return new ResponseEntity<Likes>(likes, HttpStatus.OK);
    }

    @GetMapping("/all-likes/{postId}")
    public ResponseEntity<List<Likes>> allLikesByPostId(@PathVariable Long postId) {
        List<Likes> allLikes = likeService.allLikesByPostId(postId);
        return new ResponseEntity<>(allLikes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{likesId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long likesId) {
        String message = likeService.deleteLikesById(likesId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
