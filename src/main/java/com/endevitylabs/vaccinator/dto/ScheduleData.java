package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
public record ScheduleData(
        String type,
        String description,
        @NotNull Set<DoseData> doses) {
}