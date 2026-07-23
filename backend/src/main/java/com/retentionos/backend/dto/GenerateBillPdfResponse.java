package com.retentionos.backend.dto;

public record GenerateBillPdfResponse(
        String pdfUrl,
        String businessName
) {
}
