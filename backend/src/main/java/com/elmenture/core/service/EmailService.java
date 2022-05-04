package com.elmenture.core.service;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {
    void sendAppStartedEmail();

    void sendNewOrderEmail(Long orderId, List<Long> orderItemIds, long orderTotalCents, String customerName, String deliveryAddress, String deliveryMode) throws MessagingException;
}
