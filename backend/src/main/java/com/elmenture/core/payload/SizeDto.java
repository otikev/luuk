package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 14-Apr-2022
 *
 * Used to hold mapped values that are derived from what the user has entered
 */
@Data
public class SizeDto {
    private String international;
    private int us;
    private int uk;
    private int eu;
    private String chest_cm;
    private String waist_cm;
    private String hips_cm;
}
