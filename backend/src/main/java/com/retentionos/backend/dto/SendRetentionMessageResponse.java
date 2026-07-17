package com.retentionos.backend.dto;

public record SendRetentionMessageResponse(
        boolean success,
        String message
) {
}
