package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 31-Mar-2022
 */
@Data
public class UserMeasurementsDto {
    BodyMeasurementsDto bodyMeasurements;
    ClothingSizeDto clothingSizes;
}
