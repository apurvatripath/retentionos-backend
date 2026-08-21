package com.retentionos.backend.dto;

import java.math.BigDecimal;

public record BillLineCalculation(
        String name,
        int quantity,
        double price,
        BigDecimal lineSubtotal,
        BigDecimal gstRate,
        BigDecimal taxAmount,
        BigDecimal cgst,
        BigDecimal sgst,
        BigDecimal igst
) {
}
