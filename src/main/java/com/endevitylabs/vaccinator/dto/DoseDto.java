package com.endevitylabs.vaccinator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record DoseDto(UUID id,
                      Integer doseNumber,
                      String minAge,
                      @JsonProperty("is_booster") Boolean booster,
                      String note) {
} 