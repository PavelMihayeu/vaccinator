package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.BulkLoadVaccineRequest;
import com.endevitylabs.vaccinator.dto.BulkLoadVaccineResponse;
import com.endevitylabs.vaccinator.dto.GetAllVaccinesResponse;
import com.endevitylabs.vaccinator.model.VaccineDocument;
import com.endevitylabs.vaccinator.service.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vaccines")
@Tag(name = "Vaccine API V1", description = "Vaccine endpoints with MongoDB storage")
@Slf4j
public class VaccineController {

    private final VaccineService vaccineService;

    @Autowired
    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @GetMapping
    @Operation(
            summary = "Get all vaccines (V1)",
            description = "Retrieves all vaccines stored in MongoDB with flexible JSON structure"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccines",
                    content = @Content(schema = @Schema(implementation = GetAllVaccinesResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GetAllVaccinesResponse> getAllVaccines() {
        GetAllVaccinesResponse response = vaccineService.getAllVaccines();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    @Operation(
            summary = "Get vaccine by name",
            description = "Retrieves a specific vaccine by its name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccine",
                    content = @Content(schema = @Schema(implementation = VaccineDocument.class))),
            @ApiResponse(responseCode = "404", description = "Vaccine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccineDocument> getVaccineById(@PathVariable String name) {
        VaccineDocument vaccine = vaccineService.getVaccineByName(name);
        return ResponseEntity.ok(vaccine);
    }

    @PostMapping("/bulk-load")
    @Operation(
            summary = "Bulk load vaccines (V1)",
            description = "Loads multiple vaccines from JSON data. Automatically clears existing vaccines before loading new ones."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaccines loaded successfully",
                    content = @Content(schema = @Schema(implementation = BulkLoadVaccineResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid vaccine data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BulkLoadVaccineResponse> bulkLoadVaccines(@Valid @RequestBody BulkLoadVaccineRequest request) {
        BulkLoadVaccineResponse response = vaccineService.bulkLoadVaccines(request);
        return ResponseEntity.status(201).body(response);
    }
} 