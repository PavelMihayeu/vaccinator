package com.endevitylabs.vaccinator.dto;

import java.util.List;

public record WhoGuidelineSummaryDto(
        String title,
        String url,
        List<WhoGuidelineTableDto> tables
) {
} 