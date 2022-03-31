package com.elmenture.core.service.impl;

import com.elmenture.core.model.Item;
import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by otikev on 31-Mar-2022
 */

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto postDto) {

        // convert DTO to entity
        Item post = mapToEntity(postDto);
        Item newItem = itemRepository.save(post);

        // convert entity to DTO
        ItemDto postResponse = mapToDTO(newItem);
        return postResponse;
    }
    
    @Override
    public ItemResponse getAllItems(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        // create Pageable instance
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Item> items = itemRepository.findAll(pageable);

        // get content for page object
        List<Item> listOfItems = items.getContent();

        List<ItemDto> content= listOfItems.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(content);
        itemResponse.setPageNo(items.getNumber());
        itemResponse.setPageSize(items.getSize());
        itemResponse.setTotalElements(items.getTotalElements());
        itemResponse.setTotalPages(items.getTotalPages());
        itemResponse.setLast(items.isLast());

        return itemResponse;
    }

    private ItemDto mapToDTO(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setDescription(item.getDescription());
        itemDto.setSizeInternational(item.getSizeInternational());
        itemDto.setSizeNumber(item.getSizeNumber());
        itemDto.setPrice(item.getPrice());
        if(item.getImageUrl()==null || item.getImageUrl().isEmpty()){
            itemDto.setImageUrl("https://cdn2.iconfinder.com/data/icons/pick-a-dress/900/dress-dresses-fashion-clothes-clothing-silhouette-shadow-15-512.png");//Default image
        }else{
            itemDto.setImageUrl(item.getImageUrl());
        }

        return itemDto;
    }

    private Item mapToEntity(ItemDto itemDto){
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setDescription(itemDto.getDescription());
        item.setSizeInternational(itemDto.getSizeInternational());
        item.setSizeNumber(itemDto.getSizeNumber());
        item.setPrice(itemDto.getPrice());
        item.setImageUrl(itemDto.getImageUrl());
        return item;
    }
}
