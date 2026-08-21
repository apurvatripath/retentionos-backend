package com.retentionos.backend.dto;

public record DashboardResponse(
    long totalCustomers,
    long activeCustomers,
    long inactiveCustomers
) {

}