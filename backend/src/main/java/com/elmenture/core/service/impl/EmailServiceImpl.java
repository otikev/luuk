package com.elmenture.core.service.impl;

import com.elmenture.core.service.EmailService;
import com.elmenture.core.utils.LuukProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by otikev on 23-Apr-2022
 */

//https://www.baeldung.com/spring-email
@Service
public class EmailServiceImpl implements EmailService {
    private final String[] SITE_ADMINS = {"oti.kevin@gmail.com"};

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendAppStartedEmail() {
        System.out.println("Sending app started email...");
        if(!LuukProperties.enableEmails){
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(LuukProperties.smtpUsername);
        message.setTo(SITE_ADMINS[0]);
        message.setSubject("LUUK SERVICE NOTIFICATION");
        message.setText("Luuk backend has started running");
        emailSender.send(message);
    }
}
