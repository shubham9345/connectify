package com.connectify.demo.Controller;


import com.connectify.demo.Model.Notification;
import com.connectify.demo.ServiceImpl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/all-notification/{id}")
    private ResponseEntity<?> getNotification(@PathVariable Long id){
        List<Notification> allNotification = notificationService.getNotificationByUserId(id);
        return new ResponseEntity<>(allNotification, HttpStatus.OK);
    }
}
