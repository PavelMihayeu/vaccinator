package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.dto.VaccineDto;

import java.util.List;
import java.util.UUID;

public interface VaccineService {

    List<VaccineDto> getAllVaccines();
    
    VaccineDto getVaccineById(UUID id);
    
    List<VaccineDto> searchVaccinesByName(String name);
}