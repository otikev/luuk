package com.elmenture.core.service;

import com.elmenture.core.exception.ResourceNotFoundException;
import com.elmenture.core.model.Order;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.ItemDto;
import com.elmenture.core.payload.OrderConfirmationDto;
import com.elmenture.core.payload.OrderStateUpdate;
import com.elmenture.core.payload.StkPushResponseDto;
import com.elmenture.core.utils.OrderState;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    StkPushResponseDto triggerStkPush(int amount, User user) throws IOException;

    void createOrder(User loggedInUser, List<Long> orderList, StkPushResponseDto stkPushResponse);

    OrderConfirmationDto getOrderDetailsFromMerchantId(Object request);

    void saveTransactionDetails(OrderConfirmationDto orderConfirmationDto);

    void cancelOrder(Order order);

    ResponseEntity validateOrder(List<Long> orderList, User loggedInUser);

    ResponseEntity confirmOrderPaid(User user, String merchantRequestID);

    List<Order> getOrdersByState(OrderState orderState);

    void updateOrderState(OrderStateUpdate stateUpdate) throws ResourceNotFoundException;

    Order getPaidOrderForItemId(Long itemId);

    List<ItemDto> getItems(int orderId);
}
