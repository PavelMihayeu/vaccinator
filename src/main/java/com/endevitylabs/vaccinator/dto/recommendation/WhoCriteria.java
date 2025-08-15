package com.endevitylabs.vaccinator.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhoCriteria {
    private VaccinationRecommendationRequest.AgeRange age;
    @JsonProperty("life_stage")
    private String lifeStage;
    private String sex;
    @JsonProperty("pregnancy_status")
    private String pregnancyStatus;
    @JsonProperty("risk_factors")
    private List<String> riskFactors;
    private Boolean immunocompromised;
    @JsonProperty("special_conditions")
    private List<String> specialConditions;
}
