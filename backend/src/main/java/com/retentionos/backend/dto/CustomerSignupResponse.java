package com.retentionos.backend.dto;

import com.retentionos.backend.entity.Customer;

public record CustomerSignupResponse(
        Customer customer,
        boolean checkIn
) {
}
