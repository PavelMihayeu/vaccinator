package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.PrequalifiedVaccineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrequalifiedVaccineRepository extends JpaRepository<PrequalifiedVaccineEntity, Long> {

    /**
     * Find all prequalified vaccines by vaccine type (partial match)
     */
    @Query("SELECT p FROM PrequalifiedVaccineEntity p WHERE p.vaccineType LIKE %:vaccineType%")
    List<PrequalifiedVaccineEntity> findByVaccineType(@Param("vaccineType") String vaccineType);

    /**
     * Find all prequalified vaccines by manufacturer
     */
    List<PrequalifiedVaccineEntity> findByManufacturer(String manufacturer);

    /**
     * Find all prequalified vaccines by responsible NRA
     */
    List<PrequalifiedVaccineEntity> findByResponsibleNRA(String responsibleNRA);

    /**
     * Delete all prequalified vaccines (for bulk replacement)
     */
    @Modifying
    @Query("DELETE FROM PrequalifiedVaccineEntity")
    void deleteAllPrequalifiedVaccines();

    /**
     * Count total number of prequalified vaccines
     */
    @Query("SELECT COUNT(p) FROM PrequalifiedVaccineEntity p")
    long countAllPrequalifiedVaccines();
} 