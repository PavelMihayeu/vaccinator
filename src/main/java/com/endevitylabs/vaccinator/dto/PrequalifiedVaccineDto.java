package com.endevitylabs.vaccinator.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({
        "dateOfPreQualification",
        "vaccineType",
        "commercialName",
        "presentation",
        "numberOfDoses",
        "manufacturer",
        "responsibleNRA"})
public record PrequalifiedVaccineDto(
        @NotNull String dateOfPreQualification,
        @NotNull String vaccineType,
        @NotNull String commercialName,
        String presentation,
        @NotNull Integer numberOfDoses,
        @NotNull String manufacturer,
        @NotNull String responsibleNRA
) {
} 