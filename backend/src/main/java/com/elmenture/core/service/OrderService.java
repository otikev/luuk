package com.elmenture.core.service;

import com.elmenture.core.model.Order;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.OrderConfirmationDto;
import com.elmenture.core.payload.StkPushResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    StkPushResponseDto triggerStkPush(int amount, User user) throws IOException;

    void createOrder(User loggedInUser, List<Long> orderList, StkPushResponseDto stkPushResponse);

    OrderConfirmationDto getOrderDetailsFromMerchantId(Object request);

    void saveTransactionDetails(OrderConfirmationDto orderConfirmationDto);

    void undoCreatedOrder(Order order);

    ResponseEntity validateOrder(List<Long> orderList, User loggedInUser);

    ResponseEntity confirmOrderPaid(User user, String merchantRequestID);
}
