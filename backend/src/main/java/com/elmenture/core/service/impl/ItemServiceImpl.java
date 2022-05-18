package com.elmenture.core.service.impl;

import com.elmenture.core.engine.SizeMapper;
import com.elmenture.core.model.Item;
import com.elmenture.core.model.ItemProperty;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.ItemDto;
import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.repository.ItemPropertyRepository;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.repository.TagPropertyRepository;
import com.elmenture.core.repository.UserRepository;
import com.elmenture.core.service.ItemService;
import com.elmenture.core.utils.ChunkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by otikev on 31-Mar-2022
 */

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemPropertyRepository itemPropertyRepository;

    @Autowired
    private TagPropertyRepository tagPropertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto postDto) {
        // convert DTO to entity
        Item post = mapToEntity(postDto);
        post.setTarget("f");//FIXME: Targetting only female dresses for now
        Item newItem = itemRepository.save(post);

        for (Long id : postDto.getTagProperties()) {
            ItemProperty itemProperty = new ItemProperty();
            itemProperty.setItemId(newItem.getId());
            itemProperty.setTagPropertyId(id);
            ItemProperty created = itemPropertyRepository.save(itemProperty);
            System.out.println("Created item property id " + created.getId());
        }

        // convert entity to DTO
        ItemDto postResponse = mapToDTO(newItem);
        return postResponse;
    }

    @Override
    public ItemDto updateItem(ItemDto postDto) {
        Item item = itemRepository.getById(postDto.getId());
        if (item != null) {
            Item post = mapToEntity(postDto);
            Item newItem = itemRepository.save(post);

            List<ItemProperty> deletedProperties = itemPropertyRepository.findByItemIdAndIdNotIn(item.getId(), postDto.getTagProperties());
            for (ItemProperty property : deletedProperties) {
                itemPropertyRepository.delete(property);
                System.out.println("Deleted item property id " + property.getId());
            }

            for (Long id : postDto.getTagProperties()) {
                ItemProperty itemProperty = new ItemProperty();
                itemProperty.setItemId(newItem.getId());
                itemProperty.setTagPropertyId(id);
                ItemProperty created = itemPropertyRepository.save(itemProperty);
                System.out.println("Created item property id " + created.getId());
            }

            ItemDto postResponse = mapToDTO(newItem);
            return postResponse;
        }
        return null;
    }

    private ItemResponse buildResponse(Page<Item> items) {
        // get content for page object
        List<Item> listOfItems = items.getContent();

        List<ItemDto> content = listOfItems.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(content);
        itemResponse.setPageNo(items.getNumber());
        itemResponse.setPageSize(items.getSize());
        itemResponse.setTotalElements(items.getTotalElements());
        itemResponse.setTotalPages(items.getTotalPages());
        itemResponse.setLast(items.isLast());

        return itemResponse;
    }

    @Override
    public ItemResponse getAllItems(List<String> targets, String sizeInternational, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> items = itemRepository.findByTargetInAndSizeInternationalIs(targets, sizeInternational, pageable);

        return buildResponse(items);
    }

    @Override
    public List<ItemDto> getAllItems(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchAllLike(keyword);
        List<ItemDto> response = items.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());

        return response;
    }

    @Override
    public List<ItemDto> getAllAvailableItemsBySold(List<Long> itemIds, boolean sold) {
        List<Item> items = itemRepository.findBySoldAndIdIn(sold,itemIds);
        List<ItemDto> response = items.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
        return response;
    }


    @Override
    public ItemResponse getAllItems(List<String> targets, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> items = itemRepository.findByTargetIn(targets, pageable);

        return buildResponse(items);
    }

    @Override
    public ItemResponse getAllItems(List<String> targets, boolean sold, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> items;
        if (sold) {
            items = itemRepository.findByTargetInAndSoldTrue(targets, pageable);
        } else {
            items = itemRepository.findByTargetInAndSoldFalse(targets, pageable);
        }

        return buildResponse(items);
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(mapToDTO(item));
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> getQueue(User user, boolean filter, int size) {
        List<Item> finalResults = new ArrayList<>();
        Sort sort = Sort.by("id").ascending();
        Map<String, String> userSizes = SizeMapper.mappedSizesForUser(user);

        long startTrackerPosition = user.getItemQueueTracker();
        long newTrackerPosition = startTrackerPosition;
        long offset = startTrackerPosition;
        boolean startedFromBeginning = false;
        do {
            Pageable pageable;

            List<Item> listOfItems = new ArrayList<>();
            System.out.println("Started traversing item queue at id " + offset);

            if (offset < startTrackerPosition) {
                startedFromBeginning = true;
                int limit = (int) (startTrackerPosition - offset);
                pageable = new ChunkRequest(offset, limit, sort);
            } else {
                pageable = new ChunkRequest(offset, size, sort);
            }

            Page<Item> items = itemRepository.findBySoldFalse(pageable);
            listOfItems = items.getContent();

            boolean exit = false;
            for (Item item : listOfItems) {
                newTrackerPosition = item.getId();
                if (startedFromBeginning && newTrackerPosition >= startTrackerPosition) {
                    System.out.println("Traversed the whole queue, exiting... ");
                    exit = true;
                    break;
                }
                if (filter) {
                    String itemSizeType = item.getSizeType();
                    String itemSize = "";
                    if (itemSizeType.equalsIgnoreCase("UK") || itemSizeType.equalsIgnoreCase("US") || itemSizeType.equalsIgnoreCase("EU")) {
                        itemSize = String.valueOf(item.getSizeNumber());
                    } else if (itemSizeType.equalsIgnoreCase("INT")) {
                        itemSize = item.getSizeInternational();
                    }
                    if (userSizes.get(itemSizeType).equalsIgnoreCase(itemSize)) {
                        System.out.println("Found match : " + item.getDescription());
                        finalResults.add(item);
                    }
                } else {
                    finalResults.add(item);
                }

                if (finalResults.size() >= size) {
                    System.out.println("got enough items!");
                    break;
                }
            }

            if (exit) {
                break;
            }

            if (listOfItems.size() < size && finalResults.size() < size) {
                System.out.println("Reached end of item queue at id " + newTrackerPosition + ". moving to beginning of queue.");
                offset = 0;
            } else {
                offset = newTrackerPosition;
            }
        } while (finalResults.size() < size);

        System.out.println("*** Found " + finalResults.size() + " items");

        List<ItemDto> content = finalResults.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
        updateTracker(user, newTrackerPosition);
        return content;
    }

    private void updateTracker(User user, long tracker) {
        user = userRepository.findById(user.getId()).get();
        user.setItemQueueTracker(tracker);
        userRepository.save(user);
        System.out.println("Updated user " + user.getId() + "'s item queue tracker to " + tracker);
    }

    private ItemDto mapToDTO(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setDescription(item.getDescription());
        itemDto.setSizeInternational(item.getSizeInternational());
        itemDto.setSizeNumber(item.getSizeNumber());
        itemDto.setPrice(item.getPrice());
        itemDto.setTarget(item.getTarget());
        itemDto.setSizeType(item.getSizeType());
        if (item.getImageUrl() == null || item.getImageUrl().isEmpty() || item.getImageUrl().equals("https://i.pinimg.com/236x/13/a8/b7/13a8b7ba22d77c1318eedeb1814be30d.jpg")) {
            itemDto.setImageUrl("https://cdn2.iconfinder.com/data/icons/pick-a-dress/900/dress-dresses-fashion-clothes-clothing-silhouette-shadow-15-512.png");//Default image
        } else {
            itemDto.setImageUrl(item.getImageUrl());
        }

        List<ItemProperty> properties = itemPropertyRepository.findByItemId(item.getId());
        List<Long> ids = new ArrayList<>();
        for (ItemProperty property : properties) {
            ids.add(property.getTagPropertyId());
        }
        itemDto.setTagProperties(ids);
        return itemDto;
    }

    private Item mapToEntity(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setDescription(itemDto.getDescription());
        item.setSizeInternational(itemDto.getSizeInternational());
        item.setSizeNumber(itemDto.getSizeNumber());
        item.setPrice(itemDto.getPrice());
        item.setImageUrl(itemDto.getImageUrl());
        item.setTarget(itemDto.getTarget().toLowerCase());
        item.setSizeType(itemDto.getSizeType());
        return item;
    }
}
