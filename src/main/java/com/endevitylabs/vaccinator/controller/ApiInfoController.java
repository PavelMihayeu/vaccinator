package com.endevitylabs.vaccinator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "API Information", description = "API version and health information")
public class ApiInfoController {

    @GetMapping("/info")
    @Operation(
            summary = "Get API information",
            description = "Returns information about available API versions and endpoints"
    )
    @ApiResponse(responseCode = "200", description = "API information retrieved successfully")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Vaccinator API");
        info.put("description", "API for managing WHO vaccination guidelines and recommendations");
        info.put("currentVersion", "1.0.0");
        info.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> versions = new HashMap<>();
        
        Map<String, Object> v1 = new HashMap<>();
        v1.put("status", "stable");
        v1.put("endpoints", "/api/v1/vaccines, /api/v1/admin/data");
        v1.put("description", "Current stable version with basic vaccine data endpoints");
        
        Map<String, Object> v2 = new HashMap<>();
        v2.put("status", "beta");
        v2.put("endpoints", "/api/v2/vaccines");
        v2.put("description", "Enhanced version with improved response format and metadata");
        
        versions.put("v1", v1);
        versions.put("v2", v2);
        
        info.put("versions", versions);
        
        return ResponseEntity.ok(info);
    }

    @GetMapping("/health")
    @Operation(
            summary = "Health check",
            description = "Returns the health status of the API"
    )
    @ApiResponse(responseCode = "200", description = "API is healthy")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
} 