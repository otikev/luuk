package com.elmenture.core.service;

import com.elmenture.core.model.User;
import com.elmenture.core.payload.ItemDto;
import com.elmenture.core.payload.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto postDto);

    ItemDto updateItem(ItemDto postDto);

    List<ItemDto> getAllItems();

    List<ItemDto> getQueue(User user, boolean filter, int size);

    ItemResponse getAllItems(List<String> targets, int page, int size, String sortBy, String sortDir);

    ItemResponse getAllItems(List<String> targets, boolean sold, int page, int size, String sortBy, String sortDir);

    ItemResponse getAllItems(List<String> targets, String sizeInternational, int page, int size, String sortBy, String sortDir);

    List<ItemDto> getAllItems(String keyword);

    List<ItemDto> getAllItems(List<Long> itemIds);
}
