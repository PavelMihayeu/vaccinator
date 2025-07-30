package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.Region;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Hidden
@Repository
public interface RegionRepository extends CrudRepository<Region, Integer> {

    Optional<Region> findByName(@Param("name") String name);
} 