package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.FollowersRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import com.connectify.demo.service.FollowersService;
import com.connectify.demo.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FollowersServiceImpl implements FollowersService {

    private final FollowersRepository followersRepository;
    private final UserInfoService userService;
    private final UserInfoRepository userInfoRepository;

    @Override
    public String followUser(
            Long fromUserId,
            Long toUserId
    ) {

        log.info(
                "Follow request received. fromUserId={}, toUserId={}",
                fromUserId,
                toUserId
        );

        if (fromUserId.equals(toUserId)) {

            log.warn(
                    "User {} attempted to follow themselves",
                    fromUserId
            );

            throw new RuntimeException(
                    "User cannot follow themselves"
            );
        }

        UserInfo fromUser =
                userService.getUserbyId(fromUserId);

        UserInfo toUser =
                userService.getUserbyId(toUserId);

        if (followersRepository.existsByFromAndTo(
                fromUser,
                toUser
        )) {

            log.warn(
                    "User {} is already following user {}",
                    fromUserId,
                    toUserId
            );

            throw new RuntimeException(
                    "User is already following the target user."
            );
        }

        Followers follow =
                new Followers(fromUser, toUser);

        followersRepository.save(follow);

        fromUser.setNoOfFollowing(
                fromUser.getNoOfFollowing() + 1
        );

        toUser.setNoOfFollowers(
                toUser.getNoOfFollowers() + 1
        );

        userInfoRepository.save(fromUser);
        userInfoRepository.save(toUser);

        log.info(
                "User {} successfully followed user {}",
                fromUserId,
                toUserId
        );

        return "User is Following userId - "
                + toUser.getId();
    }

    @Override
    public String unfollowUser(
            Long fromUserId,
            Long toUserId
    ) {

        log.info(
                "Unfollow request received. fromUserId={}, toUserId={}",
                fromUserId,
                toUserId
        );

        UserInfo fromUser =
                userService.getUserbyId(fromUserId);

        UserInfo toUser =
                userService.getUserbyId(toUserId);

        Followers follow =
                followersRepository.findByFromAndTo(
                                fromUser,
                                toUser
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "User {} is not following user {}",
                                    fromUserId,
                                    toUserId
                            );

                            return new RuntimeException(
                                    "User is not following the target user."
                            );
                        });

        followersRepository.delete(follow);

        fromUser.setNoOfFollowing(
                Math.max(
                        0,
                        fromUser.getNoOfFollowing() - 1
                )
        );

        toUser.setNoOfFollowers(
                Math.max(
                        0,
                        toUser.getNoOfFollowers() - 1
                )
        );

        userInfoRepository.save(fromUser);
        userInfoRepository.save(toUser);

        log.info(
                "User {} successfully unfollowed user {}",
                fromUserId,
                toUserId
        );

        return "User unfollowed userId - "
                + toUser.getId();
    }

    @Override
    @Transactional
    public List<UserInfo> getFollowers(
            Long userId
    ) {

        log.info(
                "Fetching followers for userId={}",
                userId
        );

        userService.getUserbyId(userId);

        List<UserInfo> followers =
                followersRepository.findFollowersByUserId(
                        userId
                );

        log.info(
                "Fetched {} followers for userId={}",
                followers.size(),
                userId
        );

        return followers;
    }

    @Override
    @Transactional
    public List<UserInfo> getFollowing(
            Long userId
    ) {

        log.info(
                "Fetching following list for userId={}",
                userId
        );

        userService.getUserbyId(userId);

        List<UserInfo> following =
                followersRepository.findFollowingByUserId(
                        userId
                );

        log.info(
                "Fetched {} following users for userId={}",
                following.size(),
                userId
        );

        return following;
    }
}