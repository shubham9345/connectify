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
public class FollowersController {

    @Autowired
    private FollowersService followersService;

    @PostMapping("/follow")
    public ResponseEntity<?> followUser(
            @RequestParam("fromUserId") Long fromUserId,
            @RequestParam("toUserId") Long toUserId) {
        try {
            followersService.followUser(fromUserId, toUserId);
            return ResponseEntity.ok().build();
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
            followersService.unfollowUser(fromUserId, toUserId);
            return ResponseEntity.ok().build();
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