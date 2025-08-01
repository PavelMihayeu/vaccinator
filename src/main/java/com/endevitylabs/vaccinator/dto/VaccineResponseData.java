package com.endevitylabs.vaccinator.dto;

import com.endevitylabs.vaccinator.model.VaccineDocument;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonPropertyOrder({"vaccines", "whoGuidelineSummary"})
public record VaccineResponseData(
        @Schema(description = "List of vaccine documents") List<VaccineDataDto> vaccines,
        WhoGuidelineSummary whoGuidelineSummary) {
}
