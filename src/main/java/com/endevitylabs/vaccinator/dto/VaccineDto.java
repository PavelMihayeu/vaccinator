package com.endevitylabs.vaccinator.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class VaccineDto {
    private UUID id;
    private String name;
    private String type;
    private String description;
    private String whoReferenceUrl;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> targetGroups;
    private Set<String> regions;
    private Set<String> considerations;
    private Set<VaccineScheduleDto> schedules;
} 