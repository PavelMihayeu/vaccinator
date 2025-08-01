package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.WhoGuidelineSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WhoGuidelineSummaryMongoRepository extends MongoRepository<WhoGuidelineSummaryDocument, String> {
    
    /**
     * Find the single WHO guideline summary document
     * There should only be one summary document at a time
     */
    Optional<WhoGuidelineSummaryDocument> findFirstByOrderByCreatedAtDesc();
} 