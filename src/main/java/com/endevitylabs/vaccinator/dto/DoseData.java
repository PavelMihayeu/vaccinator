package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record DoseData(
    @NotNull Integer doseNumber,
    String minAge,
    Boolean isBooster,
    String note
) {} 