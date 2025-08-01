package com.endevitylabs.vaccinator.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonPropertyOrder({"data", "version", "totalCount", "timestamp"})
@Schema(description = "Response for getting all vaccines")
public record GetAllVaccinesResponse(
        @Schema(description = "List of vaccine documents")
        VaccineResponseData data,

        @Schema(description = "API version", example = "1.0")
        String version,

        @Schema(description = "Total number of vaccines", example = "25")
        Integer totalCount,

        @Schema(description = "Timestamp when the response was generated", example = "2025-08-01T11:46:17.598")
        LocalDateTime timestamp
) {
} 