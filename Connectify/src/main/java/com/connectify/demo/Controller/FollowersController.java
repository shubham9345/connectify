package com.connectify.demo.Controller;

import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Service.FollowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FollowersController {

    @Autowired
    private FollowersService followersService;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(
            @RequestParam("fromUserId") Long fromUserId,
            @RequestParam("toUserId") Long toUserId) {
        try {
         String message = followersService.followUser(fromUserId, toUserId);
            return new ResponseEntity<>(message,HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
        try {
            List<UserInfo> followers = followersService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(
            @RequestParam("fromUserId") Long fromUserId,
            @RequestParam("toUserId") Long toUserId) {
        try {
          String message = followersService.unfollowUser(fromUserId, toUserId);
            return new ResponseEntity<>(message,HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
        try {
            List<UserInfo> following = followersService.getFollowing(userId);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}