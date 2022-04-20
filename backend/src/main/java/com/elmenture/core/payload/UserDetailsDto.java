package com.elmenture.core.payload;

import lombok.Data;

@Data
public class UserDetailsDto {
    private String name;
    private String email;
    private String contactPhoneNumber;
    private String mpesaPhoneNumber;
    private String targets;
    private String physicalAddress;
}
