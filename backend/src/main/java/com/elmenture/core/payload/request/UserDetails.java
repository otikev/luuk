package com.elmenture.core.payload.request;

import lombok.Data;

@Data
public class UserDetails {
    private String name;
    private String email;
    private String contactPhoneNumber;
    private String mpesaPhoneNumber;
    private String gender;
    private String physicalAddress;
}
