package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.VaccineDto;
import com.endevitylabs.vaccinator.service.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/vaccines")
@RequiredArgsConstructor
@Tag(name = "Public Vaccine API", description = "Public endpoints for vaccine information")
public class VaccineController {

    private final VaccineService vaccineService;

    @GetMapping
    @Operation(summary = "Get all vaccines", description = "Retrieve all available vaccines with their schedules and details")
    public ResponseEntity<List<VaccineDto>> getAllVaccines() {
        List<VaccineDto> vaccines = vaccineService.getAllVaccines();
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vaccine by ID", description = "Retrieve a specific vaccine by its ID")
    public ResponseEntity<VaccineDto> getVaccineById(
            @Parameter(description = "Vaccine ID") @PathVariable UUID id) {
        VaccineDto vaccine = vaccineService.getVaccineById(id);
        return ResponseEntity.ok(vaccine);
    }

    @GetMapping("/search")
    @Operation(summary = "Search vaccines by name", description = "Search vaccines by name (case-insensitive)")
    public ResponseEntity<List<VaccineDto>> searchVaccines(
            @Parameter(description = "Vaccine name to search for") @RequestParam String name) {
        List<VaccineDto> vaccines = vaccineService.searchVaccinesByName(name);
        return ResponseEntity.ok(vaccines);
    }
} 