package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.dto.LoadVaccineDataRequest;
import java.util.Map;

public interface DataManagementService {
    
    /**
     * Loads vaccine data from request DTO
     * @param request LoadVaccineDataRequest containing vaccine data
     * @return Map containing operation results
     */
    Map<String, Object> loadVaccineData(LoadVaccineDataRequest request);
    
    /**
     * Gets the current status of data in the database
     * @return Map containing data counts and status
     */
    Map<String, Object> getDataStatus();
    
    /**
     * Clears all vaccine data from the database
     * @return Map containing operation results
     */
    Map<String, Object> clearAllVaccineData();
} 