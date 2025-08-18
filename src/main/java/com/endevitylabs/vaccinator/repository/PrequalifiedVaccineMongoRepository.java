package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.PrequalifiedVaccineDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrequalifiedVaccineMongoRepository extends MongoRepository<PrequalifiedVaccineDocument, String> {

    /**
     * Find all prequalified vaccines by vaccine type (partial match)
     */
    @Query("{'vaccine_type': {$regex: ?0, $options: 'i'}}")
    List<PrequalifiedVaccineDocument> findByVaccineTypeContainingIgnoreCase(String vaccineType);

    /**
     * Find all prequalified vaccines by manufacturer
     */
    List<PrequalifiedVaccineDocument> findByManufacturer(String manufacturer);

    /**
     * Find all prequalified vaccines by responsible NRA
     */
    List<PrequalifiedVaccineDocument> findByResponsibleNRA(String responsibleNRA);

    /**
     * Find all prequalified vaccines by commercial name
     */
    List<PrequalifiedVaccineDocument> findByCommercialNameContainingIgnoreCase(String commercialName);

    /**
     * Find prequalified vaccines by date range
     */
    @Query("{'date_of_prequalification': {$gte: ?0, $lte: ?1}}")
    List<PrequalifiedVaccineDocument> findByDateOfPreQualificationBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

    /**
     * Count total number of prequalified vaccines
     */
    long count();

    /**
     * Delete all prequalified vaccines (for bulk replacement)
     */
    void deleteAll();
}
