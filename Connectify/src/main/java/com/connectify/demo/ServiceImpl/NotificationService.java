package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Model.Notification;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.NotificationRepository;
import com.connectify.demo.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserInfoService userInfoService;
    private final EmailService emailService;

    public List<Notification> getNotificationByUserId(Long userId) {

        log.info("Fetching notifications for userId: {}", userId);

        UserInfo user = userInfoService.getUserbyId(userId);

        List<Notification> allNotifications =
                user.getNotifications();

        Collections.reverse(allNotifications);

        log.info(
                "Total notifications fetched: {}",
                allNotifications.size()
        );

        return allNotifications;
    }
    @Async("notificationExecutor")
    public void sendNotification(
            UserInfo receiver,
            String message,
            Long byUserId,
            Post post
    ) {

        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setUser(receiver);
        notification.setTime(LocalDateTime.now());
        notification.setByUserId(byUserId);
        notification.setPost(post);

        List<Notification> notifications =
                receiver.getNotifications();

        if (notifications == null) {
            notifications = new ArrayList<>();
        }

        notifications.add(notification);

        receiver.setNotifications(notifications);

        emailService.sendEmail(receiver.getEmail(), receiver.getName(), post.getTopic(), post.getTopic(), post.getPostDescription());
            log.info("Sent notification for emailId: {}", receiver.getEmail());
        notificationRepository.save(notification);

        log.debug(
                "Notification sent to userId: {}",
                receiver.getId()
        );
    }
}
