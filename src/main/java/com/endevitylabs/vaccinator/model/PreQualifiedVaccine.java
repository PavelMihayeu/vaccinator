package com.endevitylabs.vaccinator.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "WHO prequalified vaccine information")
public class PreQualifiedVaccine {

    @Schema(description = "Manufacturer name", example = "GlaxoSmithKline")
    @NotBlank(message = "Manufacturer name is required")
    private String manufacturer;

    @Schema(description = "Commercial name", example = "Rotarix")
    @NotBlank(message = "Product name is required")
    private String commercialName;

    @Schema(description = "WHO pre-qualification date", example = "2009-01-01")
    @NotBlank(message = "Pre-Qualification date is required")
    private String dateOfPreQualification;

    @Schema(description = "Vaccine Type", example = "BCG")
    @NotBlank(message = "Vaccine Type is required")
    private String vaccineType;

    @Schema(description = "Number of doses in the product", example = "2")
    @NotNull(message = "Number of doses is required")
    private Integer numberOfDoses;

    @Schema(description = "Presentation format", example = "Liquid oral suspension")
    private String presentation;

    @Schema(description = "Responsible NRA", example = "Pharmaceutical and Medical Devices Agency")
    private String responsibleNRA;
} 