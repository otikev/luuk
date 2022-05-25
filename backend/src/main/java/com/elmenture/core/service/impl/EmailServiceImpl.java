package com.elmenture.core.service.impl;

import com.elmenture.core.service.EmailService;
import com.elmenture.core.utils.LuukProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by otikev on 23-Apr-2022
 */

//https://www.baeldung.com/spring-email
@Service
public class EmailServiceImpl implements EmailService {
    private final String[] SITE_ADMINS = {"oti.kevin@gmail.com", "aycewhispero@gmail.com"};
    private final String[] BUSINESS_ADMINS = {"k.kinyua@luukat.me"};
    private final String ORDERS_NOTIFICATIONS_EMAIL = "orders@luukat.me";

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendAppStartedEmail() {
        System.out.println("Sending app started email...");
        if (!LuukProperties.enableEmails) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(LuukProperties.smtpUsername);
        message.setTo(SITE_ADMINS);
        message.setSubject("LUUK SERVICE NOTIFICATION");
        message.setText("Luuk backend has started running");
        emailSender.send(message);
        logSent(Arrays.toString(SITE_ADMINS));
    }

    @Override
    public void sendNewOrderEmail(Long orderId, List<Integer> externalItemIds, long orderTotalCents, String customerName, String deliveryAddress, String deliveryMode) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("New Order");
        helper.setFrom(LuukProperties.smtpUsername);
        helper.setTo(ORDERS_NOTIFICATIONS_EMAIL);

        boolean html = true;

        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String emailBody =
                "Dear team, <br><br>" +
                        "Please prepare the order below for delivery: <br><br>" +
                        "<b>Date:</b> " + simpleDateFormat.format(new Date()) + "<br><br>" +
                        "<b>Order #:</b> " + orderId + "<br><br>" +
                        "<b>Total Paid:</b> KES " + (orderTotalCents / 100) + "<br><br>" +
                        "<b>Customer Name:</b> " + customerName + "<br><br>" +
                        "<b>Delivery Address:</b> " + deliveryAddress + "<br><br>" +
                        "<b>Delivery Mode:</b> " + deliveryMode+"<br><br>" +
                        "<b>Item Ids:</b><br>";

        for(Integer externalId : externalItemIds){
            emailBody=emailBody+"- "+externalId+"<br>";
        }
        helper.setText(emailBody, html);
        emailSender.send(message);
        logSent(ORDERS_NOTIFICATIONS_EMAIL);
    }

    private void logSent(String recipients) {
        System.out.println("@@@@ Sent email to " + recipients);
    }
}