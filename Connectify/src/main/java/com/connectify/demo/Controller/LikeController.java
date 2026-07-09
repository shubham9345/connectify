package com.connectify.demo.Controller;

import com.connectify.demo.Dto.LikeResponse;
import com.connectify.demo.Model.Likes;
import com.connectify.demo.ServiceImpl.LikesServiceImpl;
import com.connectify.demo.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@CrossOrigin
@RequiredArgsConstructor
public class LikeController {

    private final LikesService likeService;

    @PostMapping("/add-like/{userId}/{postId}")
    public ResponseEntity<Likes> addComment(@PathVariable Long userId, @PathVariable Long postId) {
        Likes likes = likeService.addLike(userId, postId);
        return new ResponseEntity<Likes>(likes, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<LikeResponse>> getLikesByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                likeService.allLikesByPostId(
                        postId,
                        page,
                        size
                )
        );
    }

    @DeleteMapping("/delete/{likesId}")
    public ResponseEntity<String> deleteLikesById(@PathVariable Long likesId) {
        String message = likeService.deleteLikesById(likesId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}/{postId}")
    public String removeLikeByUserId(@PathVariable Long userId, @PathVariable Long postId) {
        int ans = likeService.removeLikeByUserId(userId, postId);
        if (ans == 1) {
            return "Remove Like on post with Id - " + postId;
        } else {
            return "something wrong in remove Like on post with Id - " + postId;
        }
    }
}
