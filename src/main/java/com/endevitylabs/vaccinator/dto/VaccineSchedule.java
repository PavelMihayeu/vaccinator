package com.endevitylabs.vaccinator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vaccine schedule information for V3 API")
public class VaccineSchedule {

    @Schema(description = "Schedule name or identifier", example = "Primary Series")
    @NotBlank(message = "Schedule name is required")
    private String name;

    @Schema(description = "Age group for this schedule", example = "Infants 0-2 months")
    @NotBlank(message = "Age group is required")
    private String ageGroup;

    @Schema(description = "Number of doses required", example = "3")
    @NotNull(message = "Number of doses is required")
    private Integer numberOfDoses;

    @Schema(description = "Dosing intervals in weeks", example = "[0, 4, 8]")
    private List<Integer> dosingIntervals;

    @Schema(description = "Route of administration", example = "Intramuscular")
    @NotBlank(message = "Route of administration is required")
    private String route;

    @Schema(description = "Additional schedule notes", example = "First dose at birth")
    private String notes;
} 