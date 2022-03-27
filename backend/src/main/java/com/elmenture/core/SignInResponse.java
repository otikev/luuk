package com.elmenture.core;

import com.elmenture.core.model.BodyMeasurements;
import lombok.Data;

@Data
public class SignInResponse {
    private boolean success = false;
    private boolean isNewAccount = false;
    private String authToken;
    private String email;
    private BodyMeasurements measurements;
}