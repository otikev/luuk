package com.elmenture.core.service;

import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.service.impl.data.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto postDto);

    ItemResponse getAllItems(List<String> targets, int page, int size, String sortBy, String sortDir);

    ItemResponse getAllItems(List<String> targets, String sizeInternational, int page, int size, String sortBy, String sortDir);
}
