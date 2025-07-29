package com.endevitylabs.vaccinator.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DoseDto {
    private UUID id;
    private Integer doseNumber;
    private String minAge;
    private Boolean isBooster;
    private String note;
} 