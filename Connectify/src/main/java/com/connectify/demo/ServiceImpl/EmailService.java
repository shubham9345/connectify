package com.connectify.demo.ServiceImpl;

import Utility.EmailTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendUpdateMail(String toEmail,
                               String subject,
                               String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info(" email send successfully to {}", toEmail);
    }

    @Async("EmailServiceExecutor")
    public void sendEmail(
            String email,
            String recipientName,
            String subject,
            String heading,
            String message
    ) {

        String body = EmailTemplateUtil.buildEmailTemplate(
                recipientName,
                heading,
                message
        );

        sendUpdateMail(
                email,
                subject,
                body
        );

        log.info(
                "Email sent successfully to {} with subject {}",
                email,
                subject
        );
    }
}