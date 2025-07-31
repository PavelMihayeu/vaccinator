package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.dto.LoadVaccineDataRequest;
import com.endevitylabs.vaccinator.dto.WhoGuidelineSummaryDto;
import java.util.Map;

public interface DataManagementService {
    
    /**
     * Loads vaccine data from request DTO
     * @param request LoadVaccineDataRequest containing vaccine data
     * @return Map containing operation results
     */
    Map<String, Object> loadVaccineData(LoadVaccineDataRequest request);
    
    /**
     * Loads the default WHO vaccination data from resources
     * @return Map containing operation results
     */
    Map<String, Object> loadDefaultWhoData();
    
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
    
    /**
     * Clears all caches
     * @return Map containing operation results
     */
    Map<String, Object> clearAllCaches();
    
    /**
     * Gets the current WHO guideline summary
     * @return WHO guideline summary DTO
     */
    WhoGuidelineSummaryDto getWhoGuidelineSummary();
} 