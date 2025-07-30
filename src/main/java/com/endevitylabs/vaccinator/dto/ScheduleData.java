package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record ScheduleData(
        String type,
        String description,
        @NotNull List<DoseData> doses) {
}