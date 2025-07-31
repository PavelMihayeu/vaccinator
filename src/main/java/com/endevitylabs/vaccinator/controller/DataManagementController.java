package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.LoadVaccineDataRequest;
import com.endevitylabs.vaccinator.service.DataManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/data")
@Tag(name = "Data Management", description = "Admin endpoints for data management operations")
public class DataManagementController {

    private final DataManagementService dataManagementService;

    @Autowired
    public DataManagementController(DataManagementService dataManagementService) {
        this.dataManagementService = dataManagementService;
    }

    @PostMapping("/load-vaccines")
    @Operation(
            summary = "Load vaccine data from JSON",
            description = "Loads vaccine data including vaccines, schedules, doses, and their relationships from provided JSON"
    )
    public ResponseEntity<Map<String, Object>> loadVaccineData(@RequestBody LoadVaccineDataRequest request) {
        return ResponseEntity.ok(dataManagementService.loadVaccineData(request));
    }

    @GetMapping("/status")
    @Operation(
            summary = "Get data status",
            description = "Returns the current status of data in the database"
    )
    public ResponseEntity<Map<String, Object>> getDataStatus() {
        Map<String, Object> status = dataManagementService.getDataStatus();
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/clear-vaccines")
    @Operation(
            summary = "Clear all vaccine data",
            description = "Deletes all vaccines, schedules, and doses from the database"
    )
    public ResponseEntity<Map<String, Object>> clearAllVaccineData() {
        return ResponseEntity.ok(dataManagementService.clearAllVaccineData());
    }

    @PostMapping("/load-default-who-data")
    @Operation(
            summary = "Load default WHO vaccination data",
            description = "Loads the default WHO vaccination data from the application resources"
    )
    public ResponseEntity<Map<String, Object>> loadDefaultWhoData() {
        return ResponseEntity.ok(dataManagementService.loadDefaultWhoData());
    }
} 