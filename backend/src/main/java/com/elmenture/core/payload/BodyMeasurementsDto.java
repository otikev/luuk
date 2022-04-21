package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 14-Apr-2022
 */

@Data
public class BodyMeasurementsDto {
    private Integer chest_cm;
    private Integer waist_cm;
    private Integer hips_cm;
}
