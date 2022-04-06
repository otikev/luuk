package com.elmenture.core.service.impl.data;

import lombok.Data;

/**
 * Created by otikev on 31-Mar-2022
 */
@Data
public class UserMeasurementsDto {
    BodyMeasurementDto bodyMeasurements;
    ClothingSizeDto clothingSizes;

    @Data
    public class BodyMeasurementDto {
        private int chest;
        private int waist;
        private int hips;
    }

    @Data
    public class ClothingSizeDto {
        private String international;
        private int us;
        private int uk;
        private int eu;
    }
}
