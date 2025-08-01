package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScheduleData(
        String type,
        @NotNull List<DoseDto> doses) {
}