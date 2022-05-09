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
        Sort sort = Sort.by("id").descending();
        Map<String, String> userSizes = SizeMapper.mappedSizesForUser(user);
        long offset = user.getItemQueueTracker();
        int queueLaps = 0;

        long newTrackerPosition = 0;

        do {
            Pageable pageable = new ChunkRequest(offset, size, sort);
            Page<Item> items = itemRepository.findBySoldFalse(pageable);
            List<Item> listOfItems = items.getContent();


            if (finalResults.size() > 0 && listOfItems.size() == 0) {
                //Found some items and reached end of queue without getting enough items
                //Send the items to the user
                System.out.println("Reached end of queue with " + finalResults.size() + " items instead of " + size + ". Returning results to user.");
                break;
            } else if (finalResults.size() == 0 && listOfItems.size() == 0) {
                //Found no items and reached end of queue
                if (queueLaps < 1) {
                    //Try 1 more pass, could pick some previous items that user had seen but did not
                    System.out.println("Reached end of queue with 0 items, moving to beginning of queue.");
                    offset = 0;
                    queueLaps++;
                } else {
                    System.out.println("Reached end of queue with 0 items (started at beginning of queue), returning empty results to user.");
                    break;
                }
            }

            if (listOfItems.size() < size) {
                System.out.println("Reached end of queue!");
                queueLaps++;
            }

            for (Item item : listOfItems) {
                newTrackerPosition = item.getId();

                if (filter) {
                    String itemSizeType = item.getSizeType();
                    String itemSize = "";
                    if (itemSizeType.equalsIgnoreCase("UK") || itemSizeType.equalsIgnoreCase("US") || itemSizeType.equalsIgnoreCase("EU")) {
                        itemSize = String.valueOf(item.getSizeNumber());
                    } else if (itemSizeType.equalsIgnoreCase("INT")) {
                        itemSize = item.getSizeInternational();
                    }
                    if (userSizes.get(itemSizeType).equalsIgnoreCase(itemSize)) {
                        System.out.println("Found match!");
                        offset = item.getId();
                        finalResults.add(item);
                    }
                } else {
                    finalResults.add(item);
                }

                if (finalResults.size() >= size) {
                    //got enough items!
                    break;
                }
            }
        } while (finalResults.size() >= size);//true ==> reached expected page size
        System.out.println("*** Found "+finalResults.size()+" items");

        List<ItemDto> content = finalResults.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
        updateTracker(user, newTrackerPosition);
        return content;
    }

    private void updateTracker(User user, long tracker) {
        user = userRepository.findById(user.getId()).get();
        user.setItemQueueTracker(tracker);
        userRepository.save(user);
        System.out.println("Updated user "+user.getId()+"'s item queue tracker to "+tracker);
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
