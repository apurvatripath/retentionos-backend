package com.retentionos.backend.dto;

import java.util.List;

public record BatchSendResponse(
        int totalInactive,
        int sent,
        int failed,
        List<String> failures
) {
}
