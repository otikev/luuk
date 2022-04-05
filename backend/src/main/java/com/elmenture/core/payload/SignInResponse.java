package com.elmenture.core.payload;

import com.elmenture.core.model.BodyMeasurements;
import lombok.Data;

@Data
public class SignInResponse {
    private boolean success = false;
    private boolean isNewAccount = false;
    private String sessionKey;
    private String email;
    private BodyMeasurements measurements;
    private String s3AccessKeyId;
    private String s3SecretKeyId;
    private boolean isStaff;
}