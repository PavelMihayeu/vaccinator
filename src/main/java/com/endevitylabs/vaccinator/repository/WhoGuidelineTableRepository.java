package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.WhoGuidelineTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WhoGuidelineTableRepository extends JpaRepository<WhoGuidelineTable, UUID> {
} 