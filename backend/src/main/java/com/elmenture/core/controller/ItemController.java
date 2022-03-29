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
    public ResponseEntity<ItemCreationResponse> createItem(@Valid @RequestBody Item _item) {
        Item item = new Item();
        item.setDescription(String.valueOf(_item.getDescription()));
        item.setSizeInternational(String.valueOf(_item.getSizeInternational()));
        item.setSizeNumber(Long.parseLong(String.valueOf(_item.getSizeNumber())));
        item.setPrice(Long.parseLong(String.valueOf(_item.getPrice())));
        item.setImageUrl(String.valueOf(_item.getImageUrl()));
        itemRepository.save(item);

        return ResponseEntity.ok(new ItemCreationResponse(item.getId()));
    }

    @GetMapping("/all")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @RequestMapping("/open")
    public String open(){
        return "Is this what you expect to see?";
    }
}
