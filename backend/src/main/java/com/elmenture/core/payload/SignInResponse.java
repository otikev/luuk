package com.elmenture.core.payload;

import lombok.Data;

import java.util.List;

@Data
public class SignInResponse {
    private boolean success = false;
    private boolean isNewAccount = false;
    private String sessionKey;
    private String email;
    private UserMeasurementsDto actualMeasurements;
    private FemaleSize femaleSize;
    private String s3AccessKeyId;
    private String s3SecretKeyId;
    private String contactPhoneNumber;
    private String mobileMoneyNumber;
    private String physicalAddress;
    private String clothingRecommendations;
    private String name;
    private boolean staff;
    private List<TagPropertyDto> tagProperties;


    /**
     * Sizes that have been translated from what the user entered
     */
    @Data
    public static class FemaleSize {
        SizeDto dress;
    }
}