package com.retentionos.backend.dto;

public record BillItemRequest(
        String name,
        double price,
        int quantity
) {
}
