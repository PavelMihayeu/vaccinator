package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.CreateVaccineRequest;
import com.endevitylabs.vaccinator.dto.VaccineDto;
import com.endevitylabs.vaccinator.service.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/vaccines")
@RequiredArgsConstructor
@Tag(name = "Admin Vaccine API", description = "Admin endpoints for vaccine management (requires API key)")
@SecurityRequirement(name = "ApiKeyAuth")
public class AdminVaccineController {

    private final VaccineService vaccineService;

    @PostMapping
    @Operation(summary = "Create a new vaccine", description = "Create a new vaccine with schedules and doses")
    public ResponseEntity<VaccineDto> createVaccine(
            @Parameter(description = "Vaccine creation request") @Valid @RequestBody CreateVaccineRequest request,
            Authentication authentication) {
        String createdBy = authentication.getName();
        VaccineDto vaccine = vaccineService.createVaccine(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(vaccine);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vaccine", description = "Update an existing vaccine with new information")
    public ResponseEntity<VaccineDto> updateVaccine(
            @Parameter(description = "Vaccine ID") @PathVariable UUID id,
            @Parameter(description = "Vaccine update request") @Valid @RequestBody CreateVaccineRequest request,
            Authentication authentication) {
        String updatedBy = authentication.getName();
        VaccineDto vaccine = vaccineService.updateVaccine(id, request, updatedBy);
        return ResponseEntity.ok(vaccine);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vaccine", description = "Delete a vaccine by its ID")
    public ResponseEntity<Void> deleteVaccine(
            @Parameter(description = "Vaccine ID") @PathVariable UUID id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.noContent().build();
    }
} 