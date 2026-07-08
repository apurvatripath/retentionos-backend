package com.retentionos.backend.dto;

import java.util.List;

public record CsvImportResponse(
        int importedCount,
        int skippedCount,
        List<String> errors
) {
}
