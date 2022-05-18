package com.elmenture.core.controller;

import com.elmenture.core.model.*;
import com.elmenture.core.payload.OrderConfirmationDto;
import com.elmenture.core.payload.StkPushResponseDto;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.repository.OrderItemRepository;
import com.elmenture.core.repository.OrderRepository;
import com.elmenture.core.repository.TransactionDetailsRepository;
import com.elmenture.core.service.OrderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by otikev on 17-Mar-2022
 */
@Transactional
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> fetchOrders() {
        List<Order> orderList = orderRepository.findAllByUserId(getLoggedInUser().getId());
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PostMapping("/payment-confirmed")
    public void paymentConfirmed(@Valid @RequestBody Object request) {
        OrderConfirmationDto orderConfirmationDto =orderService.getOrderDetailsFromMerchantId(request);
        if (orderConfirmationDto.getOrder() == null)
            return;
        if (orderConfirmationDto.getCallbackResultCode() == 0) {
            orderService.saveTransactionDetails(orderConfirmationDto);
            executor.execute(() -> sendEmail(orderConfirmationDto.getOrder().getId()));
        } else {
            orderService.undoCreatedOrder(orderConfirmationDto.getOrder());
        }
    }

    private void sendEmail(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<Long> orderItemIds = new ArrayList<>();
        long totalCents = 0;
        String customerName = order.getUser().getFirstName() +" "+ order.getUser().getLastName();
        String address = order.getUser().getPhysicalAddress();

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            orderItemIds.add(orderItem.getId());
            totalCents += item.getPrice();
        }

        try {
            emailService.sendNewOrderEmail(order.getId(), orderItemIds, totalCents, customerName, address, "Standard");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/confirm")
    public ResponseEntity confirmOrder(@RequestParam(value = "merchant_request_id") String merchantRequestID) {
        return orderService.confirmOrderPaid(getLoggedInUser(), merchantRequestID);

    }

    @PostMapping("/validate")
    public ResponseEntity validateOrder(@Valid @org.springframework.web.bind.annotation.RequestBody List<Long> orderList) {
        return orderService.validateOrder(orderList, getLoggedInUser());
    }

}