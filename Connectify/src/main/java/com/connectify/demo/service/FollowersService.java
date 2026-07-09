package com.connectify.demo.service;

import com.connectify.demo.Model.UserInfo;

import java.util.List;

public interface FollowersService {

    String followUser(
            Long fromUserId,
            Long toUserId
    );

    String unfollowUser(
            Long fromUserId,
            Long toUserId
    );

    List<UserInfo> getFollowers(
            Long userId
    );

    List<UserInfo> getFollowing(
            Long userId
    );
}