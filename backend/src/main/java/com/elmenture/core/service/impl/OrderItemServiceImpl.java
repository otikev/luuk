package com.elmenture.core.service.impl;

import com.elmenture.core.model.Item;
import com.elmenture.core.model.Order;
import com.elmenture.core.model.OrderItem;
import com.elmenture.core.repository.OrderItemRepository;
import com.elmenture.core.repository.OrderRepository;
import com.elmenture.core.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by otikev on 06-Jun-2022
 */

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderItem> getOrderItemsForItemId(Long itemId) {
        return orderItemRepository.findByItemId(itemId);
    }

    @Override
    public void saveOrderItems(Order order, List<Item> itemList) {
        for (Item item : itemList) {
            OrderItem orderItem = new OrderItem(item, order);
            orderItemRepository.save(orderItem);
        }
    }
}
