package com.elmenture.core.payload;

import lombok.Data;

/**
 * Created by otikev on 21-Apr-2022
 */
@Data
public class ItemPropertyDto {
    private long id;
    private long itemId;
    private long tagPropertyId;
}
