package com.endevitylabs.vaccinator.dto;

import com.endevitylabs.vaccinator.dto.WhoGuidelineSummary;
import com.endevitylabs.vaccinator.model.WhoGuidelineSummaryDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Response for bulk vaccine loading operation")
public record BulkLoadVaccineResponse(
        @Schema(description = "Success message", example = "Bulk vaccine loading completed")
        String message,

        @Schema(description = "Number of existing vaccines that were cleared", example = "5")
        Long existingVaccinesCleared,

        @Schema(description = "Total number of vaccines processed", example = "10")
        Integer totalProcessed,

        @Schema(description = "Number of vaccines successfully loaded", example = "9")
        Integer successfullyLoaded,

        @Schema(description = "Number of vaccines that failed to load", example = "1")
        Integer failedToLoad,

        @Schema(description = "List of vaccine names that failed to load", example = "[\"Invalid Vaccine\"]")
        List<String> failedVaccines,

        @Schema(description = "WHO guideline summary that was saved", example = "WHO guideline summary object")
        WhoGuidelineSummary whoGuidelineSummary,

        @Schema(description = "Timestamp when the operation completed", example = "2025-08-01T11:46:17.598")
        LocalDateTime timestamp,

        @Schema(description = "API version", example = "1.0")
        String version
) {
} 