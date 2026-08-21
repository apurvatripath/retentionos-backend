package com.retentionos.backend.controller;

import com.retentionos.backend.entity.Item;
import com.retentionos.backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/businesses/{businessId}/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item createItem(@PathVariable Long businessId, @RequestBody Item item) {
        return itemService.createItem(businessId, item);
    }

    @GetMapping
    public List<Item> getItems(@PathVariable Long businessId) {
        return itemService.getItemsByBusiness(businessId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable Long businessId, @PathVariable Long itemId, @RequestBody Item item) {
        return itemService.updateItem(businessId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long businessId, @PathVariable Long itemId) {
        itemService.deleteItem(businessId, itemId);
    }
}
