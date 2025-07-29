package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CreateVaccineRequest {
    
    @NotBlank(message = "Vaccine name is required")
    @Size(max = 100, message = "Vaccine name must not exceed 100 characters")
    private String name;
    
    @Size(max = 50, message = "Vaccine type must not exceed 50 characters")
    private String type;
    
    private String description;
    
    private String whoReferenceUrl;
    
    private Set<String> targetGroups;
    
    private Set<String> regions;
    
    private Set<String> considerations;
    
    private Set<CreateVaccineScheduleRequest> schedules;
} 