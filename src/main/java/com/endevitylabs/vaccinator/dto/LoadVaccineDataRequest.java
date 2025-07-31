package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record LoadVaccineDataRequest(
        List<VaccineData> vaccines,
        WhoGuidelineSummaryDto whoGuidelineSummary
) {
}