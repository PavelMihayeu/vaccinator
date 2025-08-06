package com.endevitylabs.vaccinator.dto.prequalified;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for bulk loading prequalified vaccines
 */
@Schema(description = "Response for bulk loading prequalified vaccines")
public record BulkLoadPrequalifiedVaccinesResponse(
    @Schema(description = "Success status", example = "true")
    boolean success,
    
    @Schema(description = "Number of vaccines loaded", example = "278")
    int vaccinesLoaded,
    
    @Schema(description = "Number of vaccines replaced", example = "0")
    int vaccinesReplaced,
    
    @Schema(description = "Timestamp of the operation", example = "2025-01-15T10:30:00")
    LocalDateTime timestamp,
    
    @Schema(description = "List of loaded vaccines")
    List<PrequalifiedVaccineDto> vaccines,
    
    @Schema(description = "Error messages if any")
    List<String> errors
) {
} 