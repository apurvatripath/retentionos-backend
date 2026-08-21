package com.retentionos.backend.dto;

import com.retentionos.backend.entity.Business;

public record LoginResponse(
        Business business,
        String token
) {
}
