package com.elmenture.core.service.impl;

import com.elmenture.core.engine.SizeMapper;
import com.elmenture.core.engine.charts.MeasurementUnit;
import com.elmenture.core.model.*;
import com.elmenture.core.payload.ItemDto;
import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.repository.*;
import com.elmenture.core.service.ItemActionService;
import com.elmenture.core.service.ItemService;
import com.elmenture.core.service.OrderService;
import com.elmenture.core.utils.Action;
import com.elmenture.core.utils.CannedSearch;
import com.elmenture.core.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private ItemActionService itemActionService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public ItemDto createItem(ItemDto postDto) {
        // convert DTO to entity
        Item post = mapToEntity(postDto);
        post.setTarget("f");//Targetting only female dresses for now
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
        List<Item> items = itemRepository.searchAllLike(keyword, false);

        List<Long> tagIds = tagPropertyRepository.getTagPropertyIds(keyword);
        List<Long> itemIds = itemPropertyRepository.findItemIdsByTagPropertyId(tagIds);
        Set<Item> itemsTagged = new HashSet<>(itemRepository.findBySoldAndIdIn(false, itemIds));
        itemsTagged.addAll(items);

        return itemsTagged.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private List<ItemDto> goneForever(long userId) {
        List<Long> itemIds = itemActionService.getAllItemsForUserAction(Action.LIKE, userId);
        List<ItemDto> itemsLikedAndAlreadySold = getAllItemsWithIdAndSoldStatus(itemIds, true);

        List<ItemDto> boughtByOthers = new ArrayList<>();
        for (ItemDto itemDto : itemsLikedAndAlreadySold) {
            Order order = orderService.getPaidOrderForItemId(itemDto.getId());
            if (order.getUser().getId() != userId) {
                boughtByOthers.add(itemDto);
            }
        }
        return boughtByOthers;
    }

    @Override
    public List<ItemDto> getCannedItems(CannedSearch keyword, long userId) {

        switch (keyword) {
            case FAVORITES:
                return favorites(userId);
            case ON_SALE:
                return onSale(userId);
            case STYLE_WE_LOVE:
                return styleWeLove();
            case GONE_FOREVER:
                return goneForever(userId);
            case BRANDS_YOU_LOVE:
                return brandsYouLove();
            case RECENTLY_VIEWED:
                return recentlyViewed(userId);
            default:
        }
        return new ArrayList<>();
    }

    private List<ItemDto> brandsYouLove() {
        List<String> brandNames = brandRepository.findDescriptionByAwarenessOrPerception("High", "Premium");
        List<Item> items = itemRepository.findByBrandIn(brandNames);

        return removeSoldItems(items).stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
    }

    private List<ItemDto> recentlyViewed(long userId) {
        LocalDate _7DaysAgo = LocalDate.now().minusDays(7);
        List<Long> itemIds = new ArrayList<>();

        //All the items that I have swiped right OR left on in the last 1 week
        itemIds.addAll(itemActionService.getItemIdsForUserWithDateGreaterThanOrEqualTo(userId, _7DaysAgo));

        //Last 10 items that I have swiped right on older than 1 week
        itemIds.addAll(itemActionService.getItemsForUserWithDateLessThan(Action.LIKE, userId, _7DaysAgo, 10));

        //Last 10 items that I have swiped left on older than 1 week
        itemIds.addAll(itemActionService.getItemsForUserWithDateLessThan(Action.DISLIKE, userId, _7DaysAgo, 10));

        itemIds = MiscUtils.removeDuplicates(itemIds);

        List<ItemDto> items = getAllItemsWithIdAndSoldStatus(itemIds, false);

        return items;
    }

    private List<ItemDto> styleWeLove() {
        List<String> recommendation = getRecommendationByDayAndTime();
        List<Long> tagIds = tagPropertyRepository.getTagPropertyIds(recommendation);
        List<Long> itemIds = itemPropertyRepository.findItemIdsByTagPropertyId(tagIds);
        return getAllItemsWithIdAndSoldStatus(itemIds, false);
    }

    private List<String> getRecommendationByDayAndTime() {
        ArrayList<String> recommendation = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        switch (today.getDayOfWeek()) {
            case SUNDAY:
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
                recommendation.add("work dress");
                break;
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
                recommendation.add("casual");
                recommendation.add("evening dress");
                if (today.getHour() > 21 || today.getHour() < 6) {
                    recommendation.add("Sweetheart");
                    recommendation.add("Mini");
                    recommendation.add("Strapless");
                    recommendation.add("Shoulder Straps");
                }
                break;
        }
        return recommendation;
    }

    private List<ItemDto> onSale(long userId) {
        User user = userRepository.getById(userId);
        ClothingSize clothingSize = user.getClothingSize();
        String sizeType = "";
        int sizeNumber = 0;
        String sizeInternational = "";

        if (clothingSize.getInternational() != null) {
            sizeType = MeasurementUnit.INT.name();
            sizeInternational = clothingSize.getInternational();
        }

        if (clothingSize.getEu() != null) {
            sizeNumber = clothingSize.getEu();
            sizeType = MeasurementUnit.EU.name();
        }

        if (clothingSize.getUk() != null) {
            sizeNumber = clothingSize.getUk();
            sizeType = MeasurementUnit.UK.name();
        }

        if (clothingSize.getUs() != null) {
            sizeNumber = clothingSize.getUs();
            sizeType = MeasurementUnit.US.name();
        }

        long count = (long) Math.ceil(itemRepository.countOfUserSizedItems(sizeType, sizeInternational, sizeNumber) * 0.4);

        List<Item> items = itemRepository.fetchAllBySize(
                sizeType,
                sizeInternational,
                sizeNumber,
                count);

        return removeSoldItems(items).stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
    }

    private List<Item> removeSoldItems(List<Item> items) {
        List<Item> filtered = new ArrayList<>();
        for (Item item : items) {
            if (!item.getSold()) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private List<ItemDto> favorites(long userId) {
        List<Long> itemIds = itemActionService.getAllItemsForUserAction(Action.LIKE, userId);
        return getAllItemsWithIdAndSoldStatus(itemIds, false);
    }

    @Override
    public List<ItemDto> getAllItemsWithIdAndSoldStatus(List<Long> itemIds, boolean sold) {
        List<Item> items = itemRepository.findBySoldAndIdIn(sold, itemIds);
        //ensure ordering as per the itemIds list
        List<Item> ordered = new ArrayList<>(items.size());
        do {
            for (Long id : itemIds) {
                for (Item item : items) {
                    if (item.getId() == id && item.getSold() == sold) {
                        ordered.add(item);
                        items.remove(item);
                        break;
                    }
                }
            }
        } while (items.size() > 0);

        List<ItemDto> response = ordered.stream().map(item -> mapToDTO(item)).collect(Collectors.toList());
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
        Sort sort = Sort.by("id").descending();
        List<Item> items = itemRepository.findAll(sort);
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
                pageable = PageRequest.ofSize(limit).withSort(sort);
            } else {
                pageable = PageRequest.ofSize(size).withSort(sort);
            }

            Page<Item> items = itemRepository.findBySoldAndIdGreaterThan(false, offset, pageable);
            listOfItems = items.getContent();

            boolean exit = false;
            for (Item item : listOfItems) {
                newTrackerPosition = item.getId();
                if (item.getSold()) {
                    continue;//skip sold items
                }
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
        itemDto.setExternalId(item.getExternalId());
        itemDto.setBrand(item.getBrand());
        itemDto.setDescription(item.getDescription());
        itemDto.setSizeInternational(item.getSizeInternational());
        itemDto.setSizeNumber(item.getSizeNumber());
        itemDto.setPrice(item.getPrice());
        itemDto.setTarget(item.getTarget());
        itemDto.setSold(item.getSold());
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
        item.setExternalId(itemDto.getExternalId());
        item.setBrand(itemDto.getBrand());
        item.setDescription(itemDto.getDescription());
        item.setSizeInternational(itemDto.getSizeInternational());
        item.setSizeNumber(itemDto.getSizeNumber());
        item.setPrice(itemDto.getPrice());
        item.setImageUrl(itemDto.getImageUrl());
        item.setSold(itemDto.isSold());
        item.setTarget(itemDto.getTarget().toLowerCase());
        item.setSizeType(itemDto.getSizeType());
        return item;
    }
}
