package com.elmenture.core.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by otikev on 31-Mar-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private List<ItemDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
