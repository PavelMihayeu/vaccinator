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
public class PersonalizedRecommendation {
    private String vaccine;
    private String abbr;
    private String why;
    private String label;
    private String series;
    private List<Dose> doses;
    private List<String> notes;
    private List<String> rules;
    private Boolean annual;
    private Boolean singleDose;
    private String applicabilityReason;
    private List<String> citations;
}
