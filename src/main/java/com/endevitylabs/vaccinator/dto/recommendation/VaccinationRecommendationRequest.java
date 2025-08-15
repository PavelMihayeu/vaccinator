package com.endevitylabs.vaccinator.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for bulk-loading vaccination recommendations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VaccinationRecommendationRequest {
    
    private String schemaVersion;
    private String generatedAtUtc;
    private String sourcePdf;
    private String sourceNote;
    private Units units;
    private List<VaccineRecommendation> vaccines;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Units {
        private String age;
        private String intervals;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VaccineRecommendation {
        private String abbr;
        private String immunizingAgent;
        private String category;
        private String why;
        private List<Recommendation> recommendations;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recommendation {
        private String label;
        private WhoCriteria who;
        private Schedule schedule;
        private Limits limits;
        private List<String> citations;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgeRange {
        private Double minYears;
        private Double maxYears;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule {
        private String series;
        private List<Dose> doses;
        private List<Branch> branches;
        private Integer finalDoseMinAgeMonths;
        private List<String> notes;
        private List<String> rules;
        private Boolean annual;
        private Boolean singleDose;
        private Limits limits;
        private String externalScheduleRef;
        private String externalConsiderationsRef;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Branch {
        private AppliesWhen appliesWhen;
        private String series;
        private List<Dose> doses;
        private List<String> rules;
        private Integer finalDoseMinAgeMonths;
        private List<String> notes;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AppliesWhen {
        private Integer ageYearsMin;
        private Integer ageYearsMax;
        private Integer ageAtSeriesStartYearsLt;
        private Integer ageAtSeriesStartYearsGe;
        private List<String> products;
        private Boolean immunocompromised;
        private String presentation;
        private Boolean needRapidProtection;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Limits {
        private Integer maxAgeForFirstDoseMonths;
        private Integer maxAgeForFinalDoseMonths;
    }
} 