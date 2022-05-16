package com.elmenture.core.repository;

import com.elmenture.core.model.Item;
import com.elmenture.core.model.Order;
import com.elmenture.core.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> deleteByOrderId(Long orderId);

    List<Long> findItemIdByOrderId(Long orderId);

    List<OrderItem> findByOrderId(long id);
}
