package com.endevitylabs.vaccinator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record DoseDto(
        @NotNull Integer doseNumber,
        String minAge,
        @JsonProperty("isBooster") Boolean booster,
        @Null String note
) {
} 