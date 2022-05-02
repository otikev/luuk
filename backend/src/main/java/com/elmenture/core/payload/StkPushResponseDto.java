package com.elmenture.core.payload;

import lombok.Data;

@Data
public class StkPushResponseDto {
    String MerchantRequestID;
    String CheckoutRequestID;
    String ResponseCode;
    String ResponseDescription;
    String CustomerMessage;
}
