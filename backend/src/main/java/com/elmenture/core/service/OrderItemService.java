package com.elmenture.core.service;

import com.elmenture.core.model.Item;
import com.elmenture.core.model.Order;
import com.elmenture.core.model.OrderItem;
import com.elmenture.core.payload.ItemDto;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> getOrderItemsForItemId(Long itemId);// it's possible to have multiple records, e.g. for cancelled or pending orders

    void saveOrderItems(Order order, List<Item> itemList);
}
