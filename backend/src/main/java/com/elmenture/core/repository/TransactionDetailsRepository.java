package com.elmenture.core.repository;

import com.elmenture.core.model.OrderItem;
import com.elmenture.core.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {
}
