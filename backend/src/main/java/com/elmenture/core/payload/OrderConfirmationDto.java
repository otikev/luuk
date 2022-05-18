package com.elmenture.core.payload;

import com.elmenture.core.model.Order;
import lombok.Data;

import java.util.List;

/**
 * Created by otikev on 26-Apr-2022
 */

@Data
public class OrderConfirmationDto {

    private Order order;

    private int callbackResultCode;

    private String callbackMerchantId;

    double amount;

    String mpesaReceiptNumber;

    Long phoneNumber;
}