package com.connectify.demo.Service;

import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.FollowersRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowersService {

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private UserInfoService userService;

    @Transactional
    public void followUser(Long fromUserId, Long toUserId) {
        UserInfo  fromUser = userService.getUserbyId(fromUserId);
        UserInfo  toUser = userService.getUserbyId(toUserId);

        if (followersRepository.existsByFromAndTo(fromUser, toUser)) {
            throw new RuntimeException("User is already following the target user.");
        }

        Followers follow = new Followers(fromUser, toUser);
        followersRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long fromUserId, Long toUserId) {
        UserInfo fromUser = userService.getUserbyId(fromUserId);
        UserInfo toUser = userService.getUserbyId(toUserId);

        Followers follow = followersRepository.findByFromAndTo(fromUser, toUser)
                .orElseThrow(() -> new RuntimeException("User is not following the target user."));

        followersRepository.delete(follow);
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