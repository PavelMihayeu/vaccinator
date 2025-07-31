package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.VaccineDto;
import com.endevitylabs.vaccinator.dto.VaccineApiResponse;
import com.endevitylabs.vaccinator.service.VaccineService;
import com.endevitylabs.vaccinator.service.DataManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;
    private final DataManagementService dataManagementService;

    @Autowired
    public VaccineController(VaccineService vaccineService, DataManagementService dataManagementService) {
        this.vaccineService = vaccineService;
        this.dataManagementService = dataManagementService;
    }

    @GetMapping
    @Operation(
            summary = "Get all vaccines",
            description = "Retrieves all vaccines with their schedules, doses, and relationships"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccines",
                    content = @Content(schema = @Schema(implementation = VaccineApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccineApiResponse> getAllVaccines() {
        List<VaccineDto> vaccines = vaccineService.getAllVaccines();
        var whoGuidelineSummary = dataManagementService.getWhoGuidelineSummary();
        
        VaccineApiResponse response = new VaccineApiResponse(vaccines, whoGuidelineSummary);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get vaccine by ID",
            description = "Retrieves a specific vaccine by its UUID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccine",
                    content = @Content(schema = @Schema(implementation = VaccineDto.class))),
            @ApiResponse(responseCode = "404", description = "Vaccine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccineDto> getVaccineById(@PathVariable UUID id) {
        try {
            VaccineDto vaccine = vaccineService.getVaccineById(id);
            return ResponseEntity.ok(vaccine);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search vaccines by name",
            description = "Searches for vaccines by name (case-insensitive partial match)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccines",
                    content = @Content(schema = @Schema(implementation = VaccineApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccineApiResponse> searchVaccinesByName(@RequestParam String name) {
        List<VaccineDto> vaccines = vaccineService.searchVaccinesByName(name);
        var whoGuidelineSummary = dataManagementService.getWhoGuidelineSummary();
        
        VaccineApiResponse response = new VaccineApiResponse(vaccines, whoGuidelineSummary);
        return ResponseEntity.ok(response);
    }
} 