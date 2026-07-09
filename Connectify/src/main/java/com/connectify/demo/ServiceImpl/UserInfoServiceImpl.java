package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Dto.SignupRequest;
import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.UserInfoRepository;
import com.connectify.demo.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfo AddUser(SignupRequest signupRequest) {

        log.info("User registration request received for username: {}", signupRequest.getUsername());

        UserInfo existingUser = userInfoRepository.findByUsername(signupRequest.getUsername());

        if (existingUser != null) {
            log.warn("User registration failed. Username already exists: {}", signupRequest.getUsername());
            throw new RuntimeException("username already exist!!");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(signupRequest.getUsername());
        userInfo.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userInfo.setEmail(signupRequest.getEmail());
        userInfo.setRoles(signupRequest.getRoles());
        userInfo.setName(userInfo.getUsername());
        userInfo.setUserBio(signupRequest.getUserBio());
        userInfo.setUrl(signupRequest.getUrl());

        userInfoRepository.save(userInfo);

        log.info(
                "User registered successfully. UserId: {}, Username: {}",
                userInfo.getId(),
                userInfo.getUsername()
        );

        return userInfo;
    }

    public UserInfo getUserbyId(Long id) {

        log.debug("Fetching user with id: {}", id);

        Optional<UserInfo> userOptional = userInfoRepository.findById(id);

        if (userOptional.isEmpty()) {

            log.error("User not found with id: {}", id);

            throw new UserNotFoundException(
                    "User not found with Id " + id,
                    "user is not found!! check it once"
            );
        }

        log.debug("User fetched successfully with id: {}", id);

        return userOptional.get();
    }

    @Override
    @Transactional
    public Page<UserInfo> getAllUser(int page, int size) {

        log.info(
                "Fetching users with pagination. Page: {}, Size: {}",
                page,
                size
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "username")
        );

        Page<UserInfo> users =
                userInfoRepository.findAll(pageable);

        log.info(
                "Fetched {} users out of {} total users",
                users.getNumberOfElements(),
                users.getTotalElements()
        );

        return users;
    }

    public String deleteUserById(Long userId) {

        log.info("Delete request received for user id: {}", userId);

        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Delete failed. User not found with id: {}", userId);
                    return new UserNotFoundException(
                            "User not found with Id: " + userId,
                            "user is not found!! check it once"
                    );
                });

        userInfoRepository.delete(user);

        log.info(
                "User deleted successfully. UserId: {}, Username: {}",
                userId,
                user.getUsername()
        );

        return "User deleted successfully with id: " + userId;
    }

    public UserInfo updatedUser(UserInfo userInfo, Long userId) {

        log.info("Update request received for user id: {}", userId);

        UserInfo existingUser = getUserbyId(userId);

        if (existingUser == null) {

            log.error("Update failed. User not found with id: {}", userId);

            throw new UserNotFoundException(
                    "User is not found with Id - " + userId,
                    "user is not found!! check it once"
            );
        }

        if (userInfo.getUserBio() != null) {
            existingUser.setUserBio(userInfo.getUserBio());
        }

        if (userInfo.getName() != null) {
            existingUser.setName(userInfo.getName());
        }

        if (userInfo.getEmail() != null) {
            existingUser.setEmail(userInfo.getEmail());
        }

        if (userInfo.getPassword() != null) {
            existingUser.setPassword(
                    passwordEncoder.encode(userInfo.getPassword())
            );
        }

        if (userInfo.getUsername() != null) {
            existingUser.setUsername(userInfo.getUsername());
        }

        if (userInfo.getUrl() != null) {
            existingUser.setUrl(userInfo.getUrl());
        }

        userInfoRepository.save(existingUser);

        log.info(
                "User updated successfully. UserId: {}, Username: {}",
                existingUser.getId(),
                existingUser.getUsername()
        );

        return existingUser;
    }
}