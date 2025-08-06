package com.endevitylabs.vaccinator.dto.prequalified;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO for bulk loading prequalified vaccines from CSV
 */
@Schema(description = "Request for bulk loading prequalified vaccines from CSV file")
public record BulkLoadPrequalifiedVaccinesRequest(
    @Schema(description = "Whether to replace existing vaccines", example = "true")
    boolean replaceExisting
) {
} 