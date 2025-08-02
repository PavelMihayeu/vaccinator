package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.dto.BulkLoadVaccineRequest;
import com.endevitylabs.vaccinator.dto.BulkLoadVaccineResponse;
import com.endevitylabs.vaccinator.dto.GetAllVaccinesResponse;
import com.endevitylabs.vaccinator.model.VaccineDocument;

public interface VaccineService {

    /**
     * Get all vaccines from MongoDB
     */
    GetAllVaccinesResponse getAllVaccines();

    /**
     * Get a specific vaccine by name
     */
    VaccineDocument getVaccineByName(String name);

    /**
     * Bulk load vaccines from JSON data
     */
    BulkLoadVaccineResponse bulkLoadVaccines(BulkLoadVaccineRequest request);
}