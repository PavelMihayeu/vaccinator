package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record VaccineData(
        @NotNull @NotBlank String name,
        String type,
        String description,
        String whoReferenceUrl,
        @NotNull List<String> targetGroups,
        @NotNull List<String> regions,
        @NotNull List<String> considerations,
        @NotNull List<ScheduleData> schedules
) {
}