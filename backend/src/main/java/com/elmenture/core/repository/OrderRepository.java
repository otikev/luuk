package com.elmenture.core.repository;

import com.elmenture.core.model.Order;
import com.elmenture.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByMerchantRequestID(String merchantRequestID);

    public List<Order> findAllByUserId(Long userId);
    public Order findByUserAndState(User user, String state);
    public Order findByUserAndMerchantRequestIDAndState(User user, String merchantRequestID, String state);
}
