package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.WhoGuidelineSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WhoGuidelineSummaryRepository extends JpaRepository<WhoGuidelineSummary, UUID> {
} 