package com.endevitylabs.vaccinator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "WHO guideline summary with tables")
public class WhoGuidelineSummary {

    @Schema(description = "Document title", example = "WHO Recommendations for Routine Immunization – A User's Guide to the Summary Tables")
    @NotBlank(message = "Document title is required")
    private String title;

    @Schema(description = "Document URL", example = "https://www.who.int/publications/m/item/who-recommendations-for-routine-immunization--a-user-s-guide-to-the-summary-tables")
    @NotBlank(message = "Document URL is required")
    private String url;

    @Schema(description = "List of WHO guideline tables")
    @NotEmpty(message = "Tables list cannot be empty")
    private List<WhoGuidelineTable> tables;
} 