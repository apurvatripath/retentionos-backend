package com.retentionos.backend.dto;

import java.util.List;

public record GenerateBillRequest(
        List<BillItemRequest> items,
        String customerState
) {
}
