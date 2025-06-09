package com.connectify.demo.Service;

import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.FollowersRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowersService {

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private UserInfoService userService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public String followUser(Long fromUserId, Long toUserId) {
        UserInfo fromUser = userService.getUserbyId(fromUserId);
        UserInfo toUser = userService.getUserbyId(toUserId);

        if (followersRepository.existsByFromAndTo(fromUser, toUser)) {
            throw new RuntimeException("User is already following the target user.");
        }
        fromUser.setNoOfFollowing(fromUser.getFollowing().size() + 1);
        toUser.setNoOfFollowers(toUser.getFollowers().size() + 1);

        Followers follow = new Followers(fromUser, toUser);
        followersRepository.save(follow);
        userInfoRepository.save(fromUser);
        userInfoRepository.save(toUser);

        return "User is Following to userId - " + toUser.getId();
    }

    @Transactional
    public String unfollowUser(Long fromUserId, Long toUserId) {
        UserInfo fromUser = userService.getUserbyId(fromUserId);
        UserInfo toUser = userService.getUserbyId(toUserId);

        Followers follow = followersRepository.findByFromAndTo(fromUser, toUser)
                .orElseThrow(() -> new RuntimeException("User is not following the target user."));
        fromUser.getFollowing().remove(follow);
        toUser.getFollowers().remove(follow);
        fromUser.setNoOfFollowing(fromUser.getFollowing().size());
        toUser.setNoOfFollowers(toUser.getFollowers().size());
        userInfoRepository.save(fromUser);
        userInfoRepository.save(toUser);
        followersRepository.delete(follow);

        return "User is Unfollow userId - " + toUser.getId();
    }

    @Transactional
    public List<UserInfo> getFollowers(Long userId) {
        UserInfo user = userService.getUserbyId(userId);
        return followersRepository.findByTo(user).stream()
                .map(Followers::getFrom)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserInfo> getFollowing(Long userId) {
        UserInfo user = userService.getUserbyId(userId);
        return followersRepository.findByFrom(user).stream()
                .map(Followers::getTo)
                .collect(Collectors.toList());
    }
}