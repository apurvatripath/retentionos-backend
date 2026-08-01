package com.retentionos.backend.service;

import com.retentionos.backend.entity.Business;
import com.retentionos.backend.entity.Item;
import com.retentionos.backend.exception.ResourceNotFoundException;
import com.retentionos.backend.repository.BusinessRepository;
import com.retentionos.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final BusinessRepository businessRepository;

    public Item createItem(Long businessId, Item item) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Item name is required");
        }

        item.setId(null);
        item.setBusiness(business);
        item.setCreatedAt(LocalDateTime.now());
        return itemRepository.save(item);
    }

    public List<Item> getItemsByBusiness(Long businessId) {
        return itemRepository.findByBusinessId(businessId);
    }

    public Item updateItem(Long businessId, Long itemId, Item updates) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!item.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException("Item not found");
        }

        item.setPrice(updates.getPrice());
        item.setGstRate(updates.getGstRate());
        return itemRepository.save(item);
    }

    public void deleteItem(Long businessId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!item.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException("Item not found");
        }

        itemRepository.delete(item);
    }
}
