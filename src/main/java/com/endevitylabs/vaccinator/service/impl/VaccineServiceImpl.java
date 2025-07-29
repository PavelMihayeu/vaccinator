package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.CreateVaccineRequest;
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

    @Override
    public VaccineDto createVaccine(CreateVaccineRequest request, String createdBy) {
        // Check if vaccine with same name already exists
        if (vaccineRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Vaccine with name '" + request.getName() + "' already exists");
        }

        Vaccine vaccine = vaccineMapper.toEntity(request);
        vaccine.setCreatedBy(createdBy);
        vaccine.setUpdatedBy(createdBy);

        // Set relationships
        setVaccineRelationships(vaccine, request);

        // Save schedules and doses
        if (request.getSchedules() != null) {
            request.getSchedules().forEach(scheduleRequest -> {
                VaccineSchedule schedule = vaccineMapper.toEntity(scheduleRequest);
                schedule.setVaccine(vaccine);
                schedule.setCreatedBy(createdBy);
                schedule.setUpdatedBy(createdBy);

                if (scheduleRequest.getDoses() != null) {
                    scheduleRequest.getDoses().forEach(doseRequest -> {
                        Dose dose = vaccineMapper.toEntity(doseRequest);
                        dose.setSchedule(schedule);
                        schedule.getDoses().add(dose);
                    });
                }

                vaccine.getSchedules().add(schedule);
            });
        }

        Vaccine savedVaccine = vaccineRepository.save(vaccine);
        return vaccineMapper.toDto(savedVaccine);
    }

    @Override
    public VaccineDto updateVaccine(UUID id, CreateVaccineRequest request, String updatedBy) {
        Vaccine existingVaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));

        // Update basic fields
        existingVaccine.setName(request.getName());
        existingVaccine.setType(request.getType());
        existingVaccine.setDescription(request.getDescription());
        existingVaccine.setWhoReferenceUrl(request.getWhoReferenceUrl());
        existingVaccine.setUpdatedBy(updatedBy);

        // Update relationships
        setVaccineRelationships(existingVaccine, request);

        // Clear existing schedules and add new ones
        existingVaccine.getSchedules().clear();
        if (request.getSchedules() != null) {
            request.getSchedules().forEach(scheduleRequest -> {
                VaccineSchedule schedule = vaccineMapper.toEntity(scheduleRequest);
                schedule.setVaccine(existingVaccine);
                schedule.setCreatedBy(updatedBy);
                schedule.setUpdatedBy(updatedBy);

                if (scheduleRequest.getDoses() != null) {
                    scheduleRequest.getDoses().forEach(doseRequest -> {
                        Dose dose = vaccineMapper.toEntity(doseRequest);
                        dose.setSchedule(schedule);
                        schedule.getDoses().add(dose);
                    });
                }

                existingVaccine.getSchedules().add(schedule);
            });
        }

        Vaccine savedVaccine = vaccineRepository.save(existingVaccine);
        return vaccineMapper.toDto(savedVaccine);
    }

    @Override
    public void deleteVaccine(UUID id) {
        if (!vaccineRepository.existsById(id)) {
            throw new RuntimeException("Vaccine not found with id: " + id);
        }
        vaccineRepository.deleteById(id);
    }

    private void setVaccineRelationships(Vaccine vaccine, CreateVaccineRequest request) {
        // Set target groups
        if (request.getTargetGroups() != null) {
            Set<AgeGroup> targetGroups = new HashSet<>();
            request.getTargetGroups().forEach(groupName -> {
                AgeGroup ageGroup = ageGroupRepository.findByName(groupName)
                        .orElseThrow(() -> new RuntimeException("Age group not found: " + groupName));
                targetGroups.add(ageGroup);
            });
            vaccine.setTargetGroups(targetGroups);
        }

        // Set regions
        if (request.getRegions() != null) {
            Set<Region> regions = new HashSet<>();
            request.getRegions().forEach(regionName -> {
                Region region = regionRepository.findByName(regionName)
                        .orElseThrow(() -> new RuntimeException("Region not found: " + regionName));
                regions.add(region);
            });
            vaccine.setRegions(regions);
        }

        // Set considerations
        if (request.getConsiderations() != null) {
            Set<Consideration> considerations = new HashSet<>();
            request.getConsiderations().forEach(considerationName -> {
                Consideration consideration = considerationRepository.findByName(considerationName)
                        .orElseThrow(() -> new RuntimeException("Consideration not found: " + considerationName));
                considerations.add(consideration);
            });
            vaccine.setConsiderations(considerations);
        }
    }
} 