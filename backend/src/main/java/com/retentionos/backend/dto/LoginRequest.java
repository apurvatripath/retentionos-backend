package com.retentionos.backend.dto;

public record LoginRequest(
        String phone,
        String password
) {
}
