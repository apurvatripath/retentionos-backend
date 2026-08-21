package com.retentionos.backend.dto;

public record RetentionMessageResponse(
        String customerName,
        String phone,
        String message
) {
}