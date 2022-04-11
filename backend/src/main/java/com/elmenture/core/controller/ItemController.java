package com.elmenture.core.controller;

import com.elmenture.core.model.Item;
import com.elmenture.core.payload.ItemResponse;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.service.ItemService;
import com.elmenture.core.service.impl.data.ItemDto;
import com.elmenture.core.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Created by otikev on 17-Mar-2022
 */

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @PostMapping("/new")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.createItem(itemDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/paginated")
    public ResponseEntity<ItemResponse>  getItemsPaginated(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                           @RequestParam(value = "target", defaultValue = "female", required = false) String target) {
        //First page is 0
        ItemResponse response = itemService.getAllItems(pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, "desc");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/open")
    public String open() {
        return "Is this what you expect to see?";
    }
}