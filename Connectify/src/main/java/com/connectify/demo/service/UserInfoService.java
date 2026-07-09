package com.connectify.demo.service;

import com.connectify.demo.Dto.SignupRequest;
import com.connectify.demo.Model.UserInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserInfoService {

    UserInfo AddUser(SignupRequest signupRequest);

    UserInfo getUserbyId(Long id);

    Page<UserInfo> getAllUser(int page, int size);

    String deleteUserById(Long userId);

    UserInfo updatedUser(UserInfo userInfo, Long userId);
}