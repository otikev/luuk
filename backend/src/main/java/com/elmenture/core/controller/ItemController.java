package com.elmenture.core.controller;

import com.elmenture.core.ItemCreationResponse;
import com.elmenture.core.model.Item;
import com.elmenture.core.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by otikev on 17-Mar-2022
 */

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/new")
    public ResponseEntity<ItemCreationResponse> createItem(@Valid @RequestParam MultiValueMap<String, String> _item) {
        Item item = new Item();
        item.setDescription(String.valueOf(_item.get("description")));
        item.setSizeInternational(String.valueOf(_item.get("size_international")));
        item.setSizeNumber(Long.parseLong(String.valueOf(_item.get("size_number"))));
        item.setPrice(Long.parseLong(String.valueOf(_item.get("price"))));
        itemRepository.save(item);

        return ResponseEntity.ok(new ItemCreationResponse(item.getId()));
    }

    @GetMapping("/all")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
