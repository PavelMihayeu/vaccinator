package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.*;
import com.endevitylabs.vaccinator.model.*;
import com.endevitylabs.vaccinator.repository.*;
import com.endevitylabs.vaccinator.service.DataManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataManagementServiceImpl implements DataManagementService {

    private final VaccineRepository vaccineRepository;
    private final AgeGroupRepository ageGroupRepository;
    private final RegionRepository regionRepository;
    private final ConsiderationRepository considerationRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private final DoseRepository doseRepository;

    @Override
    @Transactional
    public Map<String, Object> loadVaccineData(LoadVaccineDataRequest request) {
        try {
            log.info("Starting to load vaccine data from request");

            List<VaccineData> vaccines = request.vaccines();

            if (vaccines == null || vaccines.isEmpty()) {
                throw new IllegalArgumentException("No vaccines found in request data");
            }

            // Clear existing vaccine data first
            log.info("Clearing existing vaccine data before loading new data");
            long dosesDeleted = doseRepository.count();
            doseRepository.deleteAll();

            long schedulesDeleted = vaccineScheduleRepository.count();
            vaccineScheduleRepository.deleteAll();

            long vaccinesDeleted = vaccineRepository.count();
            vaccineRepository.deleteAll();

            log.info("Cleared {} vaccines, {} schedules, {} doses", vaccinesDeleted, schedulesDeleted, dosesDeleted);

            // Load lookup data first
            Map<String, AgeGroup> ageGroups = loadAgeGroups(vaccines);
            Map<String, Region> regions = loadRegions(vaccines);
            Map<String, Consideration> considerations = loadConsiderations(vaccines);

            // Load vaccines and their relationships
            List<Vaccine> savedVaccines = new ArrayList<>();
            List<String> failedVaccines = new ArrayList<>();

            for (VaccineData vaccineData : vaccines) {
                try {
                    Vaccine vaccine = loadVaccine(vaccineData, ageGroups, regions, considerations);
                    savedVaccines.add(vaccine);
                } catch (Exception e) {
                    String vaccineName = vaccineData.name();
                    log.error("Failed to load vaccine '{}': {}", vaccineName, e.getMessage(), e);
                    failedVaccines.add(vaccineName);
                    // Don't re-throw the exception to continue processing other vaccines
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("message", "Vaccine data loaded successfully");
            result.put("vaccinesLoaded", savedVaccines.size());
            result.put("vaccinesFailed", failedVaccines.size());
            result.put("failedVaccines", failedVaccines);
            result.put("ageGroupsLoaded", ageGroups.size());
            result.put("regionsLoaded", regions.size());
            result.put("considerationsLoaded", considerations.size());
            result.put("previousDataCleared", vaccinesDeleted + schedulesDeleted + dosesDeleted);

            log.info("Successfully loaded {} vaccines, failed to load {} vaccines", savedVaccines.size(), failedVaccines.size());
            return result;

        } catch (Exception e) {
            log.error("Error loading vaccine data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load vaccine data: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDataStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("vaccines", vaccineRepository.count());
        status.put("ageGroups", ageGroupRepository.count());
        status.put("regions", regionRepository.count());
        status.put("considerations", considerationRepository.count());
        status.put("schedules", vaccineScheduleRepository.count());
        status.put("doses", doseRepository.count());
        status.put("timestamp", LocalDateTime.now());
        return status;
    }

    @Override
    @Transactional
    public Map<String, Object> clearAllVaccineData() {
        try {
            log.info("Clearing all vaccine data from database");

            long dosesDeleted = doseRepository.count();
            doseRepository.deleteAll();

            long schedulesDeleted = vaccineScheduleRepository.count();
            vaccineScheduleRepository.deleteAll();

            long vaccinesDeleted = vaccineRepository.count();
            vaccineRepository.deleteAll();

            Map<String, Object> result = new HashMap<>();
            result.put("message", "All vaccine data cleared successfully");
            result.put("vaccinesDeleted", vaccinesDeleted);
            result.put("schedulesDeleted", schedulesDeleted);
            result.put("dosesDeleted", dosesDeleted);

            log.info("Cleared {} vaccines, {} schedules, {} doses", vaccinesDeleted, schedulesDeleted, dosesDeleted);
            return result;

        } catch (Exception e) {
            log.error("Error clearing vaccine data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to clear vaccine data: " + e.getMessage(), e);
        }
    }

    private Map<String, AgeGroup> loadAgeGroups(List<VaccineData> vaccines) {
        Set<String> ageGroupNames = new HashSet<>();

        // Extract all age group names from vaccines
        for (VaccineData vaccine : vaccines) {
            List<String> targetGroups = vaccine.targetGroups();
            if (targetGroups != null) {
                ageGroupNames.addAll(targetGroups);
            }
        }

        // Load or create age groups
        Map<String, AgeGroup> ageGroups = new HashMap<>();
        for (String name : ageGroupNames) {
            AgeGroup ageGroup = ageGroupRepository.findByName(name)
                    .orElseGet(() -> {
                        AgeGroup newAgeGroup = new AgeGroup();
                        newAgeGroup.setName(name);
                        return ageGroupRepository.save(newAgeGroup);
                    });
            ageGroups.put(name, ageGroup);
        }

        return ageGroups;
    }

    private Map<String, Region> loadRegions(List<VaccineData> vaccines) {
        Set<String> regionNames = new HashSet<>();

        // Extract all region names from vaccines
        for (VaccineData vaccine : vaccines) {
            List<String> regions = vaccine.regions();
            if (regions != null) {
                regionNames.addAll(regions);
            }
        }

        // Load or create regions
        Map<String, Region> regions = new HashMap<>();
        for (String name : regionNames) {
            Region region = regionRepository.findByName(name)
                    .orElseGet(() -> {
                        Region newRegion = new Region();
                        newRegion.setName(name);
                        return regionRepository.save(newRegion);
                    });
            regions.put(name, region);
        }

        return regions;
    }

    private Map<String, Consideration> loadConsiderations(List<VaccineData> vaccines) {
        Set<String> considerationNames = new HashSet<>();

        // Extract all consideration names from vaccines
        for (VaccineData vaccine : vaccines) {
            List<String> considerations = vaccine.considerations();
            if (considerations != null) {
                considerationNames.addAll(considerations);
            }
        }

        // Load or create considerations
        Map<String, Consideration> considerations = new HashMap<>();
        for (String name : considerationNames) {
            Consideration consideration = considerationRepository.findByName(name)
                    .orElseGet(() -> {
                        Consideration newConsideration = new Consideration();
                        newConsideration.setName(name);
                        newConsideration.setDescription("Auto-generated from vaccine data");
                        return considerationRepository.save(newConsideration);
                    });
            considerations.put(name, consideration);
        }

        return considerations;
    }

    private Vaccine loadVaccine(VaccineData vaccineData,
                                Map<String, AgeGroup> ageGroups,
                                Map<String, Region> regions,
                                Map<String, Consideration> considerations) {

        String vaccineName = vaccineData.name();

        Set<Region> vaccineRegions = vaccineData.regions() != null ? vaccineData.regions().stream()
                .map(regions::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()) : new HashSet<>();

        Set<AgeGroup> targetGroups = vaccineData.targetGroups() != null ? vaccineData.targetGroups().stream()
                .map(ageGroups::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()) : new HashSet<>();

        Set<Consideration> vaccineConsiderations = vaccineData.considerations() != null ? vaccineData.considerations().stream()
                .map(considerations::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()) : new HashSet<>();

        // Create vaccine
        Vaccine vaccine = new Vaccine();
        vaccine.setName(vaccineName);
        vaccine.setType(vaccineData.type());
        vaccine.setDescription(vaccineData.description());
        vaccine.setWhoReferenceUrl(vaccineData.whoReferenceUrl());

        vaccine.setTargetGroups(targetGroups);
        vaccine.setRegions(vaccineRegions);
        vaccine.setConsiderations(vaccineConsiderations);

        vaccineRepository.save(vaccine);

        vaccineData.schedules()
                .forEach(scheduleData -> loadVaccineSchedule(vaccine, scheduleData));

        return vaccine;
    }

    private void loadVaccineSchedule(Vaccine vaccine, ScheduleData scheduleData) {

        VaccineSchedule schedule = new VaccineSchedule();
        schedule.setVaccine(vaccine);
        schedule.setScheduleType(scheduleData.type());
        schedule.setDescription(scheduleData.description());

        vaccineScheduleRepository.save(schedule);

        scheduleData.doses().forEach(doseData -> loadDose(schedule, doseData));
    }

    private void loadDose(VaccineSchedule schedule, DoseData doseData) {
        try {
            Dose dose = new Dose();
            dose.setSchedule(schedule);
            dose.setDoseNumber(doseData.doseNumber());
            dose.setMinAge(doseData.minAge());
            dose.setBooster(doseData.isBooster());
            dose.setNote(doseData.note());

            doseRepository.save(dose);
        } catch (Exception e) {
            log.error("Failed to load dose with number {} for schedule {}: {}",
                    doseData.doseNumber(), schedule.getScheduleType(), e.getMessage(), e);
            throw e;
        }
    }
} 