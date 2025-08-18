package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.dto.recommendation.BulkLoadResponse;
import com.endevitylabs.vaccinator.dto.recommendation.RecommendationQuery;
import com.endevitylabs.vaccinator.dto.recommendation.RecommendationsResponse;
import com.endevitylabs.vaccinator.dto.recommendation.VaccinationRecommendationRequest;

import java.util.List;

/**
 * Service for managing vaccination recommendations
 */
public interface VaccinationRecommendationService {
    
    /**
     * Bulk load vaccination recommendations from JSON
     * @param request The vaccination recommendation data
     * @return Bulk load response with statistics
     */
    BulkLoadResponse bulkLoadRecommendations(VaccinationRecommendationRequest request);
    
    /**
     * Get personalized vaccination recommendations based on criteria
     * @param query The query criteria (age, risk factors, etc.)
     * @return List of personalized recommendations
     */
    RecommendationsResponse getPersonalizedRecommendations(RecommendationQuery query);
    
    /**
     * Get all available vaccines
     * @return List of vaccine names
     */
    List<String> getAllAvailableVaccines();
    
    /**
     * Get recommendation metadata (schema version, source, etc.)
     * @return Recommendation metadata
     */
    RecommendationsResponse getRecommendationMetadata();
}


