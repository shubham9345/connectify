package com.connectify.demo.Service;

import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo AddUser(UserInfo user) {
        UserInfo existingUser = userInfoRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("username already exist!!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userInfoRepository.save(user);
    }

    public UserInfo getUserbyId(Long Id) {
        Optional<UserInfo> UserOptional = userInfoRepository.findById(Id);
        if (UserOptional.isEmpty()) {
            throw new RuntimeException("User not found with Id" + Id);
        }
        return UserOptional.get();
    }

    public List<UserInfo> getAllUser() {
        return userInfoRepository.findAll();
    }

//    public String deleteUserById(Long userId){
//        Optional<UserInfo> UserOptional = userInfoRepository.findById(userId);
//        if (UserOptional.isEmpty()) {
//            throw new RuntimeException("User not found with Id" + userId);
//        }
//        userInfoRepository.deleteById(userId);
//        return "User is deleted Successfully with id - " +userId;
//    }

    public String deleteUserById(Long userId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with Id: " + userId));

        userInfoRepository.delete(user);
        return "User deleted successfully with id: " + userId;
    }

    public UserInfo updatedUser(UserInfo userInfo, Long userId) {
        UserInfo existingUser = getUserbyId(userId);
        if (existingUser == null) {
            throw new RuntimeException("User is not found with Id - " + userId);
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
            existingUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        }
        if (userInfo.getUsername() != null) {
            existingUser.setUsername(userInfo.getUsername());
        }
        if(userInfo.getUrl()!=null){
            existingUser.setUrl(userInfo.getUrl());
        }

        userInfoRepository.save(existingUser);
        return existingUser;

    }
}
