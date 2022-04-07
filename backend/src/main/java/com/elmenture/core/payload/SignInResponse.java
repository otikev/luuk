package com.elmenture.core.payload;

import com.elmenture.core.model.BodyMeasurement;
import com.elmenture.core.model.ClothingSize;
import lombok.Data;

import javax.persistence.Column;

@Data
public class SignInResponse {
    private boolean success = false;
    private boolean isNewAccount = false;
    private String sessionKey;
    private String email;
    private UserMeasurements userMeasurements;
    private String s3AccessKeyId;
    private String s3SecretKeyId;
    private String contactPhoneNumber;
    private String mobileMoneyNumber;
    private String physicalAddress;
    private String gender;
    private String name;
    private boolean staff;
    @Data
    public static class UserMeasurements {
        BodyMeasurement bodyMeasurement;
        ClothingSize clothingSize;
    }
}