package com.retentionos.backend.dto;

import java.math.BigDecimal;

public record BillItemRequest(
        String name,
        double price,
        int quantity,
        BigDecimal gstRate
) {
}
