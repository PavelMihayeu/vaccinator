package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.prequalified.BulkLoadPrequalifiedVaccinesRequest;
import com.endevitylabs.vaccinator.dto.prequalified.BulkLoadPrequalifiedVaccinesResponse;
import com.endevitylabs.vaccinator.dto.prequalified.PrequalifiedVaccineDto;
import com.endevitylabs.vaccinator.service.PrequalifiedVaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for prequalified vaccine operations
 */
@RestController
@RequestMapping("/api/v1/prequalified-vaccines")
@Tag(name = "Prequalified Vaccines", description = "Operations for WHO prequalified vaccines")
public class PrequalifiedVaccineController {

    private final PrequalifiedVaccineService service;

    public PrequalifiedVaccineController(PrequalifiedVaccineService service) {
        this.service = service;
    }

    @PostMapping("/bulk-load")
    @Operation(
        summary = "Bulk load prequalified vaccines from CSV",
        description = "Load prequalified vaccines from WHO_prequalified_vaccines.csv file. Optionally replace existing vaccines."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaccines loaded successfully",
            content = @Content(schema = @Schema(implementation = BulkLoadPrequalifiedVaccinesResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BulkLoadPrequalifiedVaccinesResponse> bulkLoadFromCsv(
            @Valid @RequestBody BulkLoadPrequalifiedVaccinesRequest request) {
        
        BulkLoadPrequalifiedVaccinesResponse response = service.bulkLoadFromCsv(request);
        
        HttpStatus status = response.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Get all prequalified vaccines",
        description = "Retrieve all prequalified vaccines from the database"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaccines retrieved successfully",
            content = @Content(schema = @Schema(implementation = PrequalifiedVaccineDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PrequalifiedVaccineDto>> getAllPrequalifiedVaccines() {
        List<PrequalifiedVaccineDto> vaccines = service.getAllPrequalifiedVaccines();
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/by-type/{vaccineType}")
    @Operation(
        summary = "Get prequalified vaccines by type",
        description = "Retrieve prequalified vaccines filtered by vaccine type"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaccines retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PrequalifiedVaccineDto>> getByVaccineType(
            @Parameter(description = "Vaccine type to filter by", example = "BCG")
            @PathVariable String vaccineType) {
        
        List<PrequalifiedVaccineDto> vaccines = service.getByVaccineType(vaccineType);
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/by-manufacturer/{manufacturer}")
    @Operation(
        summary = "Get prequalified vaccines by manufacturer",
        description = "Retrieve prequalified vaccines filtered by manufacturer"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaccines retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PrequalifiedVaccineDto>> getByManufacturer(
            @Parameter(description = "Manufacturer name to filter by", example = "Serum Institute of India Pvt. Ltd.")
            @PathVariable String manufacturer) {
        
        List<PrequalifiedVaccineDto> vaccines = service.getByManufacturer(manufacturer);
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/by-nra/{responsibleNRA}")
    @Operation(
        summary = "Get prequalified vaccines by responsible NRA",
        description = "Retrieve prequalified vaccines filtered by responsible National Regulatory Authority"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaccines retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PrequalifiedVaccineDto>> getByResponsibleNRA(
            @Parameter(description = "Responsible NRA to filter by", example = "Central Drugs Standard Control Organization")
            @PathVariable String responsibleNRA) {
        
        List<PrequalifiedVaccineDto> vaccines = service.getByResponsibleNRA(responsibleNRA);
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/count")
    @Operation(
        summary = "Get total count of prequalified vaccines",
        description = "Retrieve the total number of prequalified vaccines in the database"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> getTotalCount() {
        long count = service.getTotalCount();
        return ResponseEntity.ok(count);
    }
} 