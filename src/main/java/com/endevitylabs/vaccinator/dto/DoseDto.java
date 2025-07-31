package com.endevitylabs.vaccinator.dto;

import java.util.UUID;

public record DoseDto(UUID id,
                      Integer doseNumber,
                      String minAge,
                      Boolean isBooster,
                      String note) {
} 