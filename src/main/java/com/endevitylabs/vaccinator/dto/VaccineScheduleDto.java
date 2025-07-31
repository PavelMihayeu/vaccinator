package com.endevitylabs.vaccinator.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class VaccineScheduleDto {
    private UUID id;
    private String scheduleType;
    private String description;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<DoseDto> doses;
} 