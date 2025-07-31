package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.VaccineDto;
import com.endevitylabs.vaccinator.mapper.VaccineMapper;
import com.endevitylabs.vaccinator.repository.*;
import com.endevitylabs.vaccinator.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VaccineServiceImpl implements VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper;

    @Autowired
    public VaccineServiceImpl(VaccineRepository vaccineRepository, VaccineMapper vaccineMapper) {
        this.vaccineRepository = vaccineRepository;
        this.vaccineMapper = vaccineMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccineDto> getAllVaccines() {
        return vaccineRepository.findAllWithDetails().stream()
                .map(vaccineMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VaccineDto getVaccineById(UUID id) {
        return vaccineRepository.findByIdWithDetails(id)
                .map(vaccineMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccineDto> searchVaccinesByName(String name) {
        return vaccineRepository.findByNameContainingIgnoreCase(name).stream()
                .map(vaccineMapper::toDto)
                .toList();
    }

}