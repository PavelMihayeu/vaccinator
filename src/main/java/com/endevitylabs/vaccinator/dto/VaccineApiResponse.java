package com.endevitylabs.vaccinator.dto;

import java.util.List;

public record VaccineApiResponse(
        List<VaccineDto> vaccines,
        WhoGuidelineSummaryDto whoGuidelineSummary
) {
} 