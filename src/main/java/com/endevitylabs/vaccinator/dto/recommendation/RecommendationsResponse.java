package com.endevitylabs.vaccinator.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationsResponse {
    private List<PersonalizedRecommendation> recommendations;
    private RecommendationQuery query;
    private String schemaVersion;
    private String source;
    private LocalDateTime timestamp;
    private String apiVersion;
}
