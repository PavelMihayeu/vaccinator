package com.endevitylabs.vaccinator.dto.prequalified;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO for prequalified vaccine data
 */
@Schema(description = "Prequalified vaccine information")
public record PrequalifiedVaccineDto(
    @Schema(description = "Date of pre-qualification", example = "2025-01-16")
    LocalDate dateOfPreQualification,
    
    @Schema(description = "Type of vaccine", example = "Hepatitis B (Paediatric)")
    String vaccineType,
    
    @Schema(description = "Commercial name", example = "BEVAC®")
    String commercialName,
    
    @Schema(description = "Presentation format", example = "Vial")
    String presentation,
    
    @Schema(description = "Number of doses", example = "1")
    Integer numberOfDoses,
    
    @Schema(description = "Manufacturer name", example = "Biological E. Limited")
    String manufacturer,
    
    @Schema(description = "Responsible National Regulatory Authority", example = "Central Drugs Standard Control Organization")
    String responsibleNRA
) {
} 