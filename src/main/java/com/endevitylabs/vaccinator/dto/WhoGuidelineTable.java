package com.endevitylabs.vaccinator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "WHO guideline table information")
public class WhoGuidelineTable {

    @Schema(description = "Table title", example = "Table 1 – Recommended Routine Immunization for Children")
    @NotBlank(message = "Table title is required")
    private String title;

    @Schema(description = "Table URL", example = "https://cdn.who.int/media/docs/default-source/immunization/immunization_schedules/immunization-schedule-table-1-january-2025.pdf")
    @NotBlank(message = "Table URL is required")
    private String url;
} 