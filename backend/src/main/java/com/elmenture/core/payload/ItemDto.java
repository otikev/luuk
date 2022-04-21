package com.elmenture.core.payload;

import lombok.Data;

import java.util.List;

/**
 * Created by otikev on 31-Mar-2022
 */
@Data
public class ItemDto {
    private long id;
    private String description;
    private String sizeInternational;
    private String sizeType;
    private Long sizeNumber;
    private long price;
    private String imageUrl;
    private String target;
    private List<Long> tagProperties;
}
