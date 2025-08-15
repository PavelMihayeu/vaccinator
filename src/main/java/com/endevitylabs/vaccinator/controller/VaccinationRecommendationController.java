package com.endevitylabs.vaccinator.controller;

import com.endevitylabs.vaccinator.dto.recommendation.BulkLoadResponse;
import com.endevitylabs.vaccinator.dto.recommendation.RecommendationQuery;
import com.endevitylabs.vaccinator.dto.recommendation.RecommendationsResponse;
import com.endevitylabs.vaccinator.dto.recommendation.VaccinationRecommendationRequest;
import com.endevitylabs.vaccinator.service.VaccinationRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for vaccination recommendations
 */
@RestController
@RequestMapping("/api/v1/vaccination-recommendations")
@Tag(name = "Vaccination Recommendations", description = "Endpoints for managing and querying vaccination recommendations")
public class VaccinationRecommendationController {

    private static final Logger log = LoggerFactory.getLogger(VaccinationRecommendationController.class);

    private final VaccinationRecommendationService vaccinationRecommendationService;

    @Autowired
    public VaccinationRecommendationController(VaccinationRecommendationService vaccinationRecommendationService) {
        this.vaccinationRecommendationService = vaccinationRecommendationService;
    }

    @PostMapping("/bulk-load")
    @Operation(summary = "Bulk load vaccination recommendations", 
               description = "Loads vaccination recommendations from JSON format into MongoDB")
    public ResponseEntity<BulkLoadResponse> bulkLoadRecommendations(
            @RequestBody VaccinationRecommendationRequest request) {
        
        log.info("Received request to bulk load vaccination recommendations with {} vaccines", 
                request.getVaccines().size());
        
        try {
            BulkLoadResponse response =
                    vaccinationRecommendationService.bulkLoadRecommendations(request);
            
            log.info("Successfully loaded {} vaccination recommendations", response.getNewCount());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error during bulk load of vaccination recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/recommendations")
    @Operation(summary = "Get personalized vaccination recommendations", 
               description = "Returns personalized vaccination recommendations based on age, risk factors, and other criteria")
    public ResponseEntity<RecommendationsResponse> getPersonalizedRecommendations(
            @RequestBody RecommendationQuery query) {
        
        log.info("Received request for personalized recommendations: age={}, immunocompromised={}, lifeStage={}", 
                query.getAgeYears(), query.getImmunocompromised(), query.getLifeStage());
        
        try {
            var response = vaccinationRecommendationService.getPersonalizedRecommendations(query);
            
            log.info("Found {} applicable recommendations", response.getRecommendations().size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting personalized recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get personalized vaccination recommendations via query parameters", 
               description = "Returns personalized vaccination recommendations using query parameters")
    public ResponseEntity<RecommendationsResponse> getRecommendationsByParams(
            @RequestParam(required = false) Double ageYears,
            @RequestParam(required = false) String lifeStage,
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String pregnancyStatus,
            @RequestParam(required = false) List<String> riskFactors,
            @RequestParam(required = false) Boolean immunocompromised,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Boolean needRapidProtection) {
        
        var query = RecommendationQuery.builder()
                        .ageYears(ageYears)
                        .lifeStage(lifeStage)
                        .sex(sex)
                        .pregnancyStatus(pregnancyStatus)
                        .riskFactors(riskFactors)
                        .specialConditions(null) // Add this field
                        .immunocompromised(immunocompromised)
                        .product(product)
                        .needRapidProtection(needRapidProtection)
                        .build();
        
        return getPersonalizedRecommendations(query);
    }

    @GetMapping("/vaccines")
    @Operation(summary = "Get all available vaccines", 
               description = "Returns a list of all available vaccine names")
    public ResponseEntity<List<String>> getAllAvailableVaccines() {
        log.info("Received request for all available vaccines");
        
        try {
            List<String> vaccines = vaccinationRecommendationService.getAllAvailableVaccines();
            log.info("Found {} available vaccines", vaccines.size());
            return ResponseEntity.ok(vaccines);
            
        } catch (Exception e) {
            log.error("Error getting available vaccines: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/metadata")
    @Operation(summary = "Get recommendation metadata", 
               description = "Returns metadata about the current vaccination recommendations (schema version, source, etc.)")
    public ResponseEntity<RecommendationsResponse> getRecommendationMetadata() {
        log.info("Received request for recommendation metadata");
        
        try {
            RecommendationsResponse response =
                    vaccinationRecommendationService.getRecommendationMetadata();
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting recommendation metadata: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
