package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.VaccineDto;
import com.endevitylabs.vaccinator.service.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2/vaccines")
@Tag(name = "Vaccine API V2", description = "Version 2 of vaccine endpoints with enhanced features")
public class VaccineControllerV2 {

    private final VaccineService vaccineService;

    @Autowired
    public VaccineControllerV2(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @GetMapping
    @Operation(
            summary = "Get all vaccines (V2)",
            description = "Retrieves all vaccines with enhanced response format and additional metadata"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccines",
                    content = @Content(schema = @Schema(implementation = VaccineDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getAllVaccines() {
        List<VaccineDto> vaccines = vaccineService.getAllVaccines();
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", vaccines);
        response.put("version", "2.0");
        response.put("totalCount", vaccines.size());
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get vaccine by ID (V2)",
            description = "Retrieves a specific vaccine by its UUID with enhanced error handling"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccine",
                    content = @Content(schema = @Schema(implementation = VaccineDto.class))),
            @ApiResponse(responseCode = "404", description = "Vaccine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getVaccineById(@PathVariable UUID id) {
        try {
            VaccineDto vaccine = vaccineService.getVaccineById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", vaccine);
            response.put("version", "2.0");
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Vaccine not found");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("version", "2.0");
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search vaccines by name (V2)",
            description = "Searches for vaccines by name with enhanced search capabilities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vaccines",
                    content = @Content(schema = @Schema(implementation = VaccineDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> searchVaccinesByName(@RequestParam String name) {
        List<VaccineDto> vaccines = vaccineService.searchVaccinesByName(name);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", vaccines);
        response.put("version", "2.0");
        response.put("searchTerm", name);
        response.put("totalCount", vaccines.size());
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }
} 