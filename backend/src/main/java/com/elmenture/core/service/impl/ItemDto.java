package com.elmenture.core.service.impl;

import lombok.Data;

/**
 * Created by otikev on 31-Mar-2022
 */
@Data
public class ItemDto {
    private long id;
    private String description;
    private String sizeInternational;
    private Long sizeNumber;
    private long price;
    private String imageUrl;
}
