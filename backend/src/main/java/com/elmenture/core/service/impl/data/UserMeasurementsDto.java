package com.elmenture.core.service.impl.data;

import lombok.Data;

/**
 * Created by otikev on 31-Mar-2022
 */
@Data
public class UserMeasurementsDto {
    BodyMeasurementDto bodyMeasurement;
    ClothingSizeDto clothingSize;

    @Data
    public class BodyMeasurementDto {
        private int chest_cm;
        private int waist_cm;
        private int hips_cm;
    }

    @Data
    public class ClothingSizeDto {
        private String international;
        private int us;
        private int uk;
        private int eu;
    }
}
