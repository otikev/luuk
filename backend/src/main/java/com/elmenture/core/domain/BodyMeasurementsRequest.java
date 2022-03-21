package com.elmenture.core.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BodyMeasurementsRequest {
    private String sizeInternational;
    private int sizeNumber;
}
