package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 02-Jun-2022
 */
@Data
public class OrderStateUpdate {
    private long orderId;
    private String newState;
}
