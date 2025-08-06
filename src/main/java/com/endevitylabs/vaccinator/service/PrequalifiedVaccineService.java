package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.dto.prequalified.BulkLoadPrequalifiedVaccinesRequest;
import com.endevitylabs.vaccinator.dto.prequalified.BulkLoadPrequalifiedVaccinesResponse;
import com.endevitylabs.vaccinator.dto.prequalified.PrequalifiedVaccineDto;

import java.util.List;

/**
 * Service interface for prequalified vaccine operations
 */
public interface PrequalifiedVaccineService {

    /**
     * Bulk load prequalified vaccines from CSV file
     */
    BulkLoadPrequalifiedVaccinesResponse bulkLoadFromCsv(BulkLoadPrequalifiedVaccinesRequest request);

    /**
     * Get all prequalified vaccines
     */
    List<PrequalifiedVaccineDto> getAllPrequalifiedVaccines();

    /**
     * Get prequalified vaccines by vaccine type
     */
    List<PrequalifiedVaccineDto> getByVaccineType(String vaccineType);

    /**
     * Get prequalified vaccines by manufacturer
     */
    List<PrequalifiedVaccineDto> getByManufacturer(String manufacturer);

    /**
     * Get prequalified vaccines by responsible NRA
     */
    List<PrequalifiedVaccineDto> getByResponsibleNRA(String responsibleNRA);

    /**
     * Get total count of prequalified vaccines
     */
    long getTotalCount();
} 