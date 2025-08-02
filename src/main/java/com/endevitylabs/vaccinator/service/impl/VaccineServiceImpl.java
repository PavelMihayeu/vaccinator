package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.BulkLoadVaccineRequest;
import com.endevitylabs.vaccinator.dto.BulkLoadVaccineResponse;
import com.endevitylabs.vaccinator.dto.GetAllVaccinesResponse;
import com.endevitylabs.vaccinator.dto.VaccineDataDto;
import com.endevitylabs.vaccinator.exception.DataNotFoundException;
import com.endevitylabs.vaccinator.mapper.VaccineMapper;
import com.endevitylabs.vaccinator.mapper.WhoGuidelineSummaryMapper;
import com.endevitylabs.vaccinator.model.VaccineDocument;
import com.endevitylabs.vaccinator.dto.VaccineResponseData;
import com.endevitylabs.vaccinator.model.WhoGuidelineSummaryDocument;
import com.endevitylabs.vaccinator.repository.VaccineMongoRepository;
import com.endevitylabs.vaccinator.repository.WhoGuidelineSummaryMongoRepository;
import com.endevitylabs.vaccinator.service.VaccineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VaccineServiceImpl implements VaccineService {

    private final VaccineMongoRepository vaccineMongoRepository;
    private final WhoGuidelineSummaryMongoRepository whoGuidelineSummaryMongoRepository;
    private final VaccineMapper vaccineMapper;
    private final WhoGuidelineSummaryMapper whoGuidelineSummaryMapper;

    @Autowired
    public VaccineServiceImpl(VaccineMongoRepository vaccineMongoRepository,
                            WhoGuidelineSummaryMongoRepository whoGuidelineSummaryMongoRepository,
                            VaccineMapper vaccineMapper,
                            WhoGuidelineSummaryMapper whoGuidelineSummaryMapper) {
        this.vaccineMongoRepository = vaccineMongoRepository;
        this.whoGuidelineSummaryMongoRepository = whoGuidelineSummaryMongoRepository;
        this.vaccineMapper = vaccineMapper;
        this.whoGuidelineSummaryMapper = whoGuidelineSummaryMapper;
    }

    @Override
    public GetAllVaccinesResponse getAllVaccines() {
        List<VaccineDocument> vaccines = vaccineMongoRepository.findAll();
        var vaccinesDto = vaccineMapper.toDto(vaccines);
        var guidelineSummaryDocument = whoGuidelineSummaryMongoRepository.findFirstByOrderByCreatedAtDesc();

        var guidelineSummary = guidelineSummaryDocument.map(whoGuidelineSummaryMapper::toDto).orElse(null);

        return new GetAllVaccinesResponse(
                new VaccineResponseData(vaccinesDto, guidelineSummary),
                "1.0",
                vaccines.size(),
                LocalDateTime.now()
        );
    }

    @Override
    public VaccineDocument getVaccineByName(String name) {
        return vaccineMongoRepository.findByName(name)
                .orElseThrow(() -> new DataNotFoundException("Vaccine not found with name: " + name));
    }

    @Override
    public BulkLoadVaccineResponse bulkLoadVaccines(BulkLoadVaccineRequest request) {
        try {
            log.info("Starting bulk load of vaccines");

            // Clear existing vaccines and WHO guideline summary first
            long existingCount = vaccineMongoRepository.count();
            vaccineMongoRepository.deleteAll();
            log.info("Cleared {} existing vaccines from MongoDB", existingCount);

            whoGuidelineSummaryMongoRepository.deleteAll();
            log.info("Cleared existing WHO guideline summary from MongoDB");

            List<VaccineDataDto> vaccinesData = request.vaccines();

            List<VaccineDocument> savedVaccines = new ArrayList<>();
            List<String> failedVaccines = new ArrayList<>();

            // Save WHO guideline summary separately
            WhoGuidelineSummaryDocument whoGuidelineSummaryDocument = whoGuidelineSummaryMapper.toDocument(request.whoGuidelineSummary());
            WhoGuidelineSummaryDocument savedWhoGuidelineSummary = whoGuidelineSummaryMongoRepository.save(whoGuidelineSummaryDocument);
            log.info("Saved WHO guideline summary: {}", savedWhoGuidelineSummary.getTitle());

            for (VaccineDataDto vaccineData : vaccinesData) {
                try {
                    // Use MapStruct mapper for conversion
                    VaccineDocument vaccine = vaccineMapper.toDocument(vaccineData);

                    VaccineDocument savedVaccine = vaccineMongoRepository.save(vaccine);
                    savedVaccines.add(savedVaccine);
                    log.debug("Successfully saved vaccine: {}", vaccine.getName());

                } catch (Exception e) {
                    String vaccineName = vaccineData.name();
                    log.error("Failed to save vaccine '{}': {}", vaccineName, e.getMessage(), e);
                    failedVaccines.add(vaccineName != null ? vaccineName : "Unknown");
                }
            }

            var whoGuidelineSummaryDto = whoGuidelineSummaryMapper.toDto(savedWhoGuidelineSummary);

            BulkLoadVaccineResponse response = new BulkLoadVaccineResponse(
                    "Bulk vaccine loading completed",
                    existingCount,
                    vaccinesData.size(),
                    savedVaccines.size(),
                    failedVaccines.size(),
                    failedVaccines,
                    whoGuidelineSummaryDto,
                    LocalDateTime.now(),
                    "1.0"
            );

            log.info("Bulk load completed: {} existing cleared, {} new loaded, {} failed",
                    existingCount, savedVaccines.size(), failedVaccines.size());

            return response;

        } catch (Exception e) {
            log.error("Error during bulk vaccine loading: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load vaccines: " + e.getMessage(), e);
        }
    }
}