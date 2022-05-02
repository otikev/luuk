package com.elmenture.core.payload;

import lombok.Data;

@Data
public class StkPushRequestDto {
    int BusinessShortCode;
    String Password;
    String Timestamp;
    String TransactionType;
    int Amount;
    Long PartyA;
    Long PartyB;
    Long PhoneNumber;
    String CallBackURL;
    String AccountReference;
    String TransactionDesc;
}
