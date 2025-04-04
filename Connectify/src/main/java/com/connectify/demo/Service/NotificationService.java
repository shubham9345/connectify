package com.connectify.demo.Service;

import com.connectify.demo.Model.Notification;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserInfoService userInfoService;

    public List<Notification> getNotificationByUserId(Long userId){
        UserInfo user = userInfoService.getUserbyId(userId);
        List<Notification> AllNotification = user.getNotifications();
        Collections.reverse(AllNotification);
        return AllNotification;
    }
}
