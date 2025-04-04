package com.elmenture.core.controller;

import com.elmenture.core.payload.ActionDto;
import com.elmenture.core.payload.ItemDto;
import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.service.ItemActionService;
import com.elmenture.core.service.ItemService;
import com.elmenture.core.utils.AppConstants;
import com.elmenture.core.utils.CannedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by otikev on 17-Mar-2022
 */

@RestController
@RequestMapping("/items")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemActionService itemActionService;

    @PostMapping("/new")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.createItem(itemDto), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ItemDto> updateItem(@Valid @RequestBody ItemDto itemDto) {
        ItemDto item = itemService.updateItem(itemDto);
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(item, HttpStatus.OK);
        }

    }

    @GetMapping("/all")
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/paginated")
    public ResponseEntity<ItemResponse> getItemsPaginated(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {

        //First page is 0
        ItemResponse response = itemService.getAllItems(getLoggedInUser().preferredRecommendations(), false, pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, "desc");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/queue")
    public ResponseEntity<List<ItemDto>> getQueue(@RequestParam(value = "filter", defaultValue = "true", required = false) boolean filter) {
        long start = System.currentTimeMillis();
        List<ItemDto> queue = itemService.getQueue(getLoggedInUser(), filter, 5);
        long totalTime = System.currentTimeMillis() - start;
        System.out.println("===== Generated queue in " + totalTime + " milliseconds. Filtered : " + filter + " =====");
        return new ResponseEntity<>(queue, HttpStatus.OK);
    }

    @PostMapping("/actions")
    public ResponseEntity<String> logUserActions(@Valid @RequestBody ActionDto action) {
        itemActionService.logActions(getLoggedInUser().getId(), action);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/search")
    public ResponseEntity searchItemsWithQuery(@RequestParam(value = "filter") String keyword) {
        return new ResponseEntity<>(itemService.getAllItems(keyword), HttpStatus.OK);
    }

    @GetMapping("/with-property")
    public List<ItemDto> searchItemsWithTagProperty(@RequestParam(value = "tag_property") Long tagPropertyId) {
        List<Long> itemIds = itemPropertyRepository.findItemIdByTagPropertyId(tagPropertyId);
        return itemService.getAllItemsWithIdAndSoldStatus(itemIds,false);
    }

    @GetMapping("/search/canned")
    public ResponseEntity getCannedSearch(@RequestParam(value = "can") String can) {
        CannedSearch keyword = CannedSearch.valueOf(can);
        return new ResponseEntity<>(itemService.getCannedItems(keyword, getLoggedInUser().getId()), HttpStatus.OK);
    }

    @RequestMapping("/open")
    public String open() {
        return "Is this what you expect to see?";
    }

}