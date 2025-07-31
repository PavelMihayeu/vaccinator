package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.*;
import com.endevitylabs.vaccinator.mapper.WhoGuidelineMapper;
import com.endevitylabs.vaccinator.model.*;
import com.endevitylabs.vaccinator.repository.*;
import com.endevitylabs.vaccinator.service.DataManagementService;
import com.endevitylabs.vaccinator.service.VaccineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
@Slf4j
public class DataManagementServiceImpl implements DataManagementService {

    private final VaccineRepository vaccineRepository;
    private final AgeGroupRepository ageGroupRepository;
    private final RegionRepository regionRepository;
    private final ConsiderationRepository considerationRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private final DoseRepository doseRepository;
    private final WhoGuidelineSummaryRepository whoGuidelineSummaryRepository;
    private final WhoGuidelineTableRepository whoGuidelineTableRepository;
    private final VaccineService vaccineService;
    private final WhoGuidelineMapper whoGuidelineMapper;

    @Autowired
    public DataManagementServiceImpl(VaccineRepository vaccineRepository, AgeGroupRepository ageGroupRepository, RegionRepository regionRepository, ConsiderationRepository considerationRepository, VaccineScheduleRepository vaccineScheduleRepository, DoseRepository doseRepository, WhoGuidelineSummaryRepository whoGuidelineSummaryRepository, WhoGuidelineTableRepository whoGuidelineTableRepository, VaccineService vaccineService, WhoGuidelineMapper whoGuidelineMapper) {
        this.vaccineRepository = vaccineRepository;
        this.ageGroupRepository = ageGroupRepository;
        this.regionRepository = regionRepository;
        this.considerationRepository = considerationRepository;
        this.vaccineScheduleRepository = vaccineScheduleRepository;
        this.doseRepository = doseRepository;
        this.whoGuidelineSummaryRepository = whoGuidelineSummaryRepository;
        this.whoGuidelineTableRepository = whoGuidelineTableRepository;
        this.vaccineService = vaccineService;
        this.whoGuidelineMapper = whoGuidelineMapper;
    }

    @Override
    @Transactional
    @CacheEvict(value = "whoGuidelineSummary", allEntries = true)
    public Map<String, Object> loadVaccineData(LoadVaccineDataRequest request) {
        try {
            log.info("Starting to load vaccine data from request");

            List<VaccineData> vaccines = request.vaccines();
            WhoGuidelineSummaryDto whoGuidelineSummary = request.whoGuidelineSummary();

            // Clear existing vaccine data first (delete in reverse order due to foreign key constraints)
            long previousDataCleared = clearVaccines();

            // Load WHO guideline summary if provided
            if (whoGuidelineSummary != null) {
                loadWhoGuidelineSummary(whoGuidelineSummary);
            }

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
            result.put("previousDataCleared", previousDataCleared);
            
            // Add WHO guideline summary info if loaded
            if (whoGuidelineSummary != null) {
                result.put("whoGuidelineSummaryLoaded", true);
                result.put("whoGuidelineTablesLoaded", whoGuidelineSummary.tables().size());
            } else {
                result.put("whoGuidelineSummaryLoaded", false);
                result.put("whoGuidelineTablesLoaded", 0);
            }

            // Clear cache after loading new data
            if (vaccineService instanceof VaccineServiceImpl) {
                ((VaccineServiceImpl) vaccineService).clearVaccineCache();
                log.info("Cleared vaccine cache after loading new data");
            }

            // Clear WHO guideline summary cache if WHO data was loaded
            if (whoGuidelineSummary != null) {
                // The cache will be automatically cleared by the @CacheEvict annotation
                // when the method completes, but we can log it for clarity
                log.info("WHO guideline summary cache will be cleared after loading new data");
            }

            log.info("Successfully loaded {} vaccines, failed to load {} vaccines", savedVaccines.size(), failedVaccines.size());
            return result;

        } catch (Exception e) {
            log.error("Error loading vaccine data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load vaccine data: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> loadDefaultWhoData() {
        try {
            log.info("Loading default WHO vaccination data from resources");

            // Load the JSON file from resources
            String jsonContent = new String(getClass().getResourceAsStream("/who_vaccination_data_full.json").readAllBytes());

            // Parse the JSON content
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Parse as LoadVaccineDataRequest
            LoadVaccineDataRequest request = objectMapper.readValue(jsonContent, LoadVaccineDataRequest.class);

            // Use the existing loadVaccineData method
            Map<String, Object> result = loadVaccineData(request);
            result.put("source", "WHO vaccination data (default)");

            log.info("Successfully loaded default WHO vaccination data");
            return result;

        } catch (Exception e) {
            log.error("Error loading default WHO vaccination data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load default WHO vaccination data: " + e.getMessage(), e);
        }
    }

    @Override
    @Cacheable(value = "whoGuidelineSummary", key = "'current'")
    public WhoGuidelineSummaryDto getWhoGuidelineSummary() {
        try {
            // Get the single WHO guideline summary
            Optional<WhoGuidelineSummary> summaryOptional = whoGuidelineSummaryRepository.findSingleSummary();
            if (summaryOptional.isEmpty()) {
                return null;
            }
            
            WhoGuidelineSummary summary = summaryOptional.get();
            return whoGuidelineMapper.toDto(summary);
            
        } catch (Exception e) {
            log.error("Error retrieving WHO guideline summary: {}", e.getMessage(), e);
            return null;
        }
    }

    private long clearVaccines() {

        log.info("Clearing existing vaccine and WHO guideline data before loading new data");

        long vaccinesDeleted = vaccineRepository.count();
        vaccineRepository.deleteAll();

        // Schedules and doses will be automatically deleted due to cascade
        long schedulesDeleted = vaccineScheduleRepository.count();
        long dosesDeleted = doseRepository.count();
        
        // Clear WHO guideline data
        long whoGuidelineSummariesDeleted = whoGuidelineSummaryRepository.count();
        long whoGuidelineTablesDeleted = whoGuidelineTableRepository.count();
        whoGuidelineSummaryRepository.deleteAll(); // This will cascade to tables

        log.info("Cleared {} vaccines, {} schedules, {} doses, {} WHO summaries, {} WHO tables", 
                vaccinesDeleted, schedulesDeleted, dosesDeleted, whoGuidelineSummariesDeleted, whoGuidelineTablesDeleted);

        return vaccinesDeleted + schedulesDeleted + dosesDeleted + whoGuidelineSummariesDeleted + whoGuidelineTablesDeleted;
    }

    private void loadWhoGuidelineSummary(WhoGuidelineSummaryDto whoGuidelineSummary) {
        try {
            // Clear existing WHO guideline summaries - there should only be one
            whoGuidelineSummaryRepository.deleteAll();
            log.info("Cleared existing WHO guideline summaries");
            
            // Create new summary
            WhoGuidelineSummary newSummary = new WhoGuidelineSummary();
            newSummary.setTitle(whoGuidelineSummary.title());
            newSummary.setUrl(whoGuidelineSummary.url());
            newSummary.setCreatedBy("system");
            newSummary.setUpdatedBy("system");
            
            WhoGuidelineSummary savedSummary = whoGuidelineSummaryRepository.save(newSummary);
            log.info("Saved new WHO guideline summary: {}", savedSummary.getTitle());

            // Add tables
            for (WhoGuidelineTableDto tableDto : whoGuidelineSummary.tables()) {
                WhoGuidelineTable table = new WhoGuidelineTable();
                table.setGuidelineSummary(savedSummary);
                table.setTitle(tableDto.title());
                table.setUrl(tableDto.url());
                table.setCreatedBy("system");
                table.setUpdatedBy("system");
                whoGuidelineTableRepository.save(table);
                log.info("Saved WHO guideline table: {}", table.getTitle());
            }
            
            log.info("Successfully loaded WHO guideline summary with {} tables", whoGuidelineSummary.tables().size());
        } catch (Exception e) {
            log.error("Error loading WHO guideline summary: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load WHO guideline summary: " + e.getMessage(), e);
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
        status.put("whoGuidelineSummaries", whoGuidelineSummaryRepository.count());
        status.put("whoGuidelineTables", whoGuidelineTableRepository.count());
        status.put("timestamp", LocalDateTime.now());
        return status;
    }

    @Override
    @Transactional
    public Map<String, Object> clearAllVaccineData() {
        try {
            log.info("Clearing all vaccine and WHO guideline data from database");

            long vaccinesDeleted = vaccineRepository.count();
            vaccineRepository.deleteAll();
            
            // Schedules and doses will be automatically deleted due to cascade
            long schedulesDeleted = vaccineScheduleRepository.count();
            long dosesDeleted = doseRepository.count();
            
            // Clear WHO guideline data
            long whoGuidelineSummariesDeleted = whoGuidelineSummaryRepository.count();
            long whoGuidelineTablesDeleted = whoGuidelineTableRepository.count();
            whoGuidelineSummaryRepository.deleteAll(); // This will cascade to tables

            Map<String, Object> result = new HashMap<>();
            result.put("message", "All vaccine and WHO guideline data cleared successfully");
            result.put("vaccinesDeleted", vaccinesDeleted);
            result.put("schedulesDeleted", schedulesDeleted);
            result.put("dosesDeleted", dosesDeleted);
            result.put("whoGuidelineSummariesDeleted", whoGuidelineSummariesDeleted);
            result.put("whoGuidelineTablesDeleted", whoGuidelineTablesDeleted);

            log.info("Cleared {} vaccines, {} schedules, {} doses, {} WHO summaries, {} WHO tables", 
                    vaccinesDeleted, schedulesDeleted, dosesDeleted, whoGuidelineSummariesDeleted, whoGuidelineTablesDeleted);
            return result;

        } catch (Exception e) {
            log.error("Error clearing vaccine data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to clear vaccine data: " + e.getMessage(), e);
        }
    }

    @Override
    @CacheEvict(value = {"vaccines", "whoGuidelineSummary"}, allEntries = true)
    public Map<String, Object> clearAllCaches() {
        try {
            log.info("Clearing all caches");

            // Clear vaccine cache by calling the method on VaccineService
            if (vaccineService instanceof VaccineServiceImpl) {
                ((VaccineServiceImpl) vaccineService).clearVaccineCache();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("message", "All caches cleared successfully");
            result.put("timestamp", LocalDateTime.now());

            log.info("Successfully cleared all caches");
            return result;

        } catch (Exception e) {
            log.error("Error clearing caches: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to clear caches: " + e.getMessage(), e);
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
            // Check if a dose with the same number already exists for this schedule
            Optional<Dose> existingDose = doseRepository.findByScheduleAndDoseNumber(schedule.getId(), doseData.doseNumber());
            if (existingDose.isPresent()) {
                log.warn("Dose with number {} already exists for schedule {}. Skipping duplicate.",
                        doseData.doseNumber(), schedule.getScheduleType());
                return;
            }

            Dose dose = new Dose();
            dose.setSchedule(schedule);
            dose.setDoseNumber(doseData.doseNumber());
            dose.setMinAge(doseData.minAge());
            dose.setBooster(doseData.isBooster());
            dose.setNote(doseData.note());

            Dose savedDose = doseRepository.save(dose);

            // Ensure the dose is added to the schedule's doses collection
            if (schedule.getDoses() == null) {
                schedule.setDoses(new HashSet<>());
            }
            schedule.getDoses().add(savedDose);

        } catch (Exception e) {
            log.error("Failed to load dose with number {} for schedule {}: {}",
                    doseData.doseNumber(), schedule.getScheduleType(), e.getMessage(), e);
            throw e;
        }
    }
} 