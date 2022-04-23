package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 14-Apr-2022
 */

@Data
public class ClothingSizeDto {
    private String international;
    private Integer us;
    private Integer uk;
    private Integer eu;
}
