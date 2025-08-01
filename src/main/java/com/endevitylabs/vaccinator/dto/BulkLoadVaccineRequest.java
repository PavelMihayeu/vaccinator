package com.endevitylabs.vaccinator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Bulk load request for vaccines with WHO guideline summary")
public record BulkLoadVaccineRequest(
        @Schema(description = "WHO guideline summary for the entire vaccine dataset")
        @NotNull(message = "WHO guideline summary is required")
        @Valid
        WhoGuidelineSummary whoGuidelineSummary,

        @Schema(description = "Array of vaccine data to be loaded")
        @NotEmpty(message = "Vaccines list cannot be empty")
        @Valid
        List<VaccineDataDto> vaccines
) {
} 