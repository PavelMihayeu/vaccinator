package com.endevitylabs.vaccinator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDoseRequest {
    
    @NotNull(message = "Dose number is required")
    private Integer doseNumber;
    
    @Size(max = 50, message = "Minimum age must not exceed 50 characters")
    private String minAge;
    
    private Boolean isBooster = false;
    
    private String note;
} 