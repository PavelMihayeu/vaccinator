package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.VaccinationRecommendationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB repository for vaccination recommendations
 */
@Repository
public interface VaccinationRecommendationMongoRepository extends MongoRepository<VaccinationRecommendationDocument, String> {
    
    /**
     * Find the most recent vaccination recommendation document
     * There should only be one active recommendation set at a time
     */
    Optional<VaccinationRecommendationDocument> findFirstByOrderByCreatedAtDesc();
    
    /**
     * Find by schema version
     */
    Optional<VaccinationRecommendationDocument> findBySchemaVersion(String schemaVersion);
    
    /**
     * Find by source
     */
    Optional<VaccinationRecommendationDocument> findBySource(String source);
}

