package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    Optional<Region> findByName(@Param("name") String name);
} 