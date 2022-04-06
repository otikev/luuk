package com.elmenture.core.service;

import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.service.impl.data.ItemDto;

public interface ItemService {
    ItemDto createItem(ItemDto postDto);

    ItemResponse getAllItems(int page, int size, String sortBy, String sortDir);
}
