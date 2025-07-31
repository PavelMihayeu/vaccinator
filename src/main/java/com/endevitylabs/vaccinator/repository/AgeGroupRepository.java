package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgeGroupRepository extends JpaRepository<AgeGroup, Integer> {

    Optional<AgeGroup> findByName(@Param("name") String name);
} 