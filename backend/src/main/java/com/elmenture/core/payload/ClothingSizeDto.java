package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 14-Apr-2022
 */

@Data
public class ClothingSizeDto {
    private String international;
    private int us;
    private int uk;
    private int eu;
}
