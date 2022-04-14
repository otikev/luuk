package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 14-Apr-2022
 */

@Data
public class BodyMeasurementsDto {
    private int chest_cm;
    private int waist_cm;
    private int hips_cm;
}
