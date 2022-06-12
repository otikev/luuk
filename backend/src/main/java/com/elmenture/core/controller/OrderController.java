package com.elmenture.core.controller;

import com.elmenture.core.exception.ResourceNotFoundException;
import com.elmenture.core.model.Order;
import com.elmenture.core.payload.OrderConfirmationDto;
import com.elmenture.core.payload.OrderStateUpdate;
import com.elmenture.core.repository.OrderItemRepository;
import com.elmenture.core.repository.OrderRepository;
import com.elmenture.core.service.OrderItemService;
import com.elmenture.core.service.OrderService;
import com.elmenture.core.utils.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.elmenture.core.utils.OrderState.PAID;

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

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/update-state")
    public ResponseEntity<String> updateOrderState(@Valid @RequestBody OrderStateUpdate stateUpdate) {
        try {
            OrderState newState = OrderState.valueOf(stateUpdate.getNewState());
            if (newState == PAID) {
                throw new IllegalArgumentException("User cannot update state to PAID");
            }
            orderService.updateOrderState(stateUpdate);
            return ResponseEntity.ok("Item successfully updated");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> fetchOrdersByState(@RequestParam(value = "state") String state) {
        List<Order> orderList = orderService.getOrdersByState(OrderState.getOrderState(state));
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> fetchOrders() {
        List<Order> orderList = orderRepository.findAllByUserId(getLoggedInUser().getId());
        List<Order> orders = new ArrayList<>();
        for(Order order: orderList){
            if(order.isPaid()){
                orders.add(order);
            }
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/payment-confirmed")
    public void paymentConfirmed(@Valid @RequestBody Object request) {
        OrderConfirmationDto orderConfirmationDto = orderService.getOrderDetailsFromMerchantId(request);
        if (orderConfirmationDto.getOrder() == null)
            return;
        if (orderConfirmationDto.getCallbackResultCode() == 0) {
            orderService.saveTransactionDetails(orderConfirmationDto);
            executor.execute(() -> orderService.sendNewOrderEmail(orderConfirmationDto.getOrder().getId()));
        } else {
            orderService.cancelOrder(orderConfirmationDto.getOrder());
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

    @GetMapping("/items")
    public ResponseEntity getOrderItems(@RequestParam(value = "order_id") int orderId) {
        return new ResponseEntity<>(orderService.getItems(orderId), HttpStatus.OK);
    }

}