package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateVaccineScheduleRequest {
    
    @NotBlank(message = "Schedule type is required")
    @Size(max = 50, message = "Schedule type must not exceed 50 characters")
    private String scheduleType;
    
    private String description;
    
    private List<CreateDoseRequest> doses;
} 