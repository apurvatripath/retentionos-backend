package com.retentionos.backend.dto;

public record DashboardStatsResponse(
        long totalCustomers,
        long inactiveCustomers,
        int messagesSent,
        long returningCustomers
) {
}
