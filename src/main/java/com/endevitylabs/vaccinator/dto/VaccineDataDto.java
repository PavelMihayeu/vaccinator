package com.endevitylabs.vaccinator.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@JsonPropertyOrder({
        "name",
        "type",
        "targetGroups",
        "regions",
        "schedules",
        "considerations",
        "description",
        "whoReferenceUrl",
        "prequalifiedVaccines"})
@Validated
public record VaccineDataDto(
        @NotNull @NotBlank String name,
        String type,
        @NotNull List<String> targetGroups,
        String description,
        String whoReferenceUrl,
        @NotNull List<String> regions,
        @NotNull List<String> considerations,
        @NotNull List<ScheduleData> schedules,
        List<PrequalifiedVaccineDto> prequalifiedVaccines
) {
}