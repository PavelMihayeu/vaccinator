package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.dto.prequalified.BulkLoadPrequalifiedVaccinesRequest;
import com.endevitylabs.vaccinator.dto.prequalified.BulkLoadPrequalifiedVaccinesResponse;
import com.endevitylabs.vaccinator.dto.prequalified.PrequalifiedVaccineDto;
import com.endevitylabs.vaccinator.mapper.PrequalifiedVaccineMapper;
import com.endevitylabs.vaccinator.model.PrequalifiedVaccineDocument;
import com.endevitylabs.vaccinator.repository.PrequalifiedVaccineMongoRepository;
import com.endevitylabs.vaccinator.service.PrequalifiedVaccineService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrequalifiedVaccineServiceImpl implements PrequalifiedVaccineService {

    private static final Logger logger = LoggerFactory.getLogger(PrequalifiedVaccineServiceImpl.class);
    private static final String CSV_FILE_PATH = "static/WHO_prequalified_vaccines.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PrequalifiedVaccineMongoRepository repository;
    private final PrequalifiedVaccineMapper mapper;

    public PrequalifiedVaccineServiceImpl(PrequalifiedVaccineMongoRepository repository, PrequalifiedVaccineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BulkLoadPrequalifiedVaccinesResponse bulkLoadFromCsv(BulkLoadPrequalifiedVaccinesRequest request) {
        logger.info("Starting bulk load of prequalified vaccines from CSV");
        
        List<String> errors = new ArrayList<>();
        List<PrequalifiedVaccineDocument> loadedVaccines = new ArrayList<>();
        int vaccinesReplaced = 0;

        try {
            // Delete existing vaccines if replaceExisting is true
            if (request.replaceExisting()) {
                logger.info("Deleting existing prequalified vaccines");
                repository.deleteAll();
                vaccinesReplaced = (int) repository.count();
            }

            // Load vaccines from CSV
            loadedVaccines = loadVaccinesFromCsv(errors);
            
            // Save to database
            if (!loadedVaccines.isEmpty()) {
                repository.saveAll(loadedVaccines);
                logger.info("Successfully saved {} prequalified vaccines", loadedVaccines.size());
            }

        } catch (Exception e) {
            logger.error("Error during bulk load: {}", e.getMessage(), e);
            errors.add("Failed to load vaccines: " + e.getMessage());
        }

        return new BulkLoadPrequalifiedVaccinesResponse(
            errors.isEmpty(),
            loadedVaccines.size(),
            vaccinesReplaced,
            java.time.LocalDateTime.now(),
            loadedVaccines.stream().map(mapper::toDto).collect(Collectors.toList()),
            errors
        );
    }

    private List<PrequalifiedVaccineDocument> loadVaccinesFromCsv(List<String> errors) throws IOException {
        List<PrequalifiedVaccineDocument> vaccines = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(CSV_FILE_PATH).getInputStream()))) {
            // Skip header row
            reader.readNext();
            
            String[] line;
            int lineNumber = 1; // Start from 1 since we skipped header
            
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {
                    PrequalifiedVaccineDocument vaccine = parseCsvLine(line, lineNumber);
                    vaccines.add(vaccine);
                } catch (Exception e) {
                    String error = String.format("Line %d: %s", lineNumber, e.getMessage());
                    errors.add(error);
                    logger.warn(error);
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("Error reading CSV file", e);
        }
        
        return vaccines;
    }

    private PrequalifiedVaccineDocument parseCsvLine(String[] line, int lineNumber) {
        if (line.length < 7) {
            throw new IllegalArgumentException("Invalid number of columns. Expected 7, got " + line.length);
        }

        // Parse date
        LocalDate dateOfPrequalification = parseDate(line[0].trim(), lineNumber);
        
        // Parse number of doses (can be empty)
        Integer numberOfDoses = null;
        if (line[4] != null && !line[4].trim().isEmpty()) {
            try {
                numberOfDoses = Integer.parseInt(line[4].trim());
            } catch (NumberFormatException e) {
                logger.warn("Line {}: Invalid number of doses: {}", lineNumber, line[4]);
            }
        }

        return new PrequalifiedVaccineDocument(
            dateOfPrequalification,
            line[1].trim(),
            line[2].trim(),
            line[3].trim(),
            numberOfDoses,
            line[5].trim(),
            line[6].trim()
        );
    }

    private LocalDate parseDate(String dateStr, int lineNumber) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date of prequalification is required");
        }
        
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected dd/MM/yyyy, got: " + dateStr);
        }
    }

    @Override
    public List<PrequalifiedVaccineDto> getAllPrequalifiedVaccines() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<PrequalifiedVaccineDto> getByVaccineType(String vaccineType) {
        return repository.findByVaccineTypeContainingIgnoreCase(vaccineType).stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<PrequalifiedVaccineDto> getByManufacturer(String manufacturer) {
        return repository.findByManufacturer(manufacturer).stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<PrequalifiedVaccineDto> getByResponsibleNRA(String responsibleNRA) {
        return repository.findByResponsibleNRA(responsibleNRA).stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public long getTotalCount() {
        return repository.count();
    }
} 