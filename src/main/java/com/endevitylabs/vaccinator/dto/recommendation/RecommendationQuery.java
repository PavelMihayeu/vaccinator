package com.endevitylabs.vaccinator.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationQuery {
    private Double ageYears;
    private String lifeStage;
    private String sex;
    private String pregnancyStatus;
    private List<String> riskFactors;
    private List<String> specialConditions;
    private Boolean immunocompromised;
    private String product;
    private Boolean needRapidProtection;
}
