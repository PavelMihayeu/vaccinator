package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.WhoGuidelineSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WhoGuidelineSummaryRepository extends JpaRepository<WhoGuidelineSummary, UUID> {
    
    /**
     * Find the single WHO guideline summary
     * There should always be only one summary in the system
     */
    @Query("SELECT w FROM WhoGuidelineSummary w ORDER BY w.createdAt DESC LIMIT 1")
    Optional<WhoGuidelineSummary> findSingleSummary();
} 