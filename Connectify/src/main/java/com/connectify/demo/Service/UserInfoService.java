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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserInfo savedUser = userInfoRepository.save(user);
        return savedUser;
    }

    public Optional<UserInfo> getUserbyId(Long Id) {
        Optional<UserInfo> user = userInfoRepository.findById(Id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found with Id" + Id);
        }
        return user;
    }

    public List<UserInfo> getAllUser(){
        return userInfoRepository.findAll();
    }
}
