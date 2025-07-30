package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.VaccineDto;
import com.endevitylabs.vaccinator.mapper.VaccineMapper;
import com.endevitylabs.vaccinator.model.*;
import com.endevitylabs.vaccinator.repository.*;
import com.endevitylabs.vaccinator.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VaccineServiceImpl implements VaccineService {

    private final VaccineRepository vaccineRepository;
    private final AgeGroupRepository ageGroupRepository;
    private final RegionRepository regionRepository;
    private final ConsiderationRepository considerationRepository;
    private final VaccineMapper vaccineMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VaccineDto> getAllVaccines() {
        List<Vaccine> vaccines = vaccineRepository.findAllWithDetails();
        return vaccines.stream()
                .map(vaccineMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VaccineDto getVaccineById(UUID id) {
        Vaccine vaccine = vaccineRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));
        return vaccineMapper.toDto(vaccine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccineDto> searchVaccinesByName(String name) {
        List<Vaccine> vaccines = vaccineRepository.findByNameContainingIgnoreCase(name);
        return vaccines.stream()
                .map(vaccineMapper::toDto)
                .toList();
    }

}