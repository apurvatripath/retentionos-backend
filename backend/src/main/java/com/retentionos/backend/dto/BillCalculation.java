package com.retentionos.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record BillCalculation(
        List<BillLineCalculation> lines,
        boolean hasTax,
        boolean interState,
        BigDecimal subtotal,
        BigDecimal totalCgst,
        BigDecimal totalSgst,
        BigDecimal totalIgst,
        BigDecimal grandTotal
) {
}
