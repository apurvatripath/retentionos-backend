package com.retentionos.backend.dto;

public record SetPasswordRequest(
        String phone,
        String password
) {
}
