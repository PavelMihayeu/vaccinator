package com.endevitylabs.vaccinator.model;

import com.endevitylabs.vaccinator.dto.recommendation.VaccinationRecommendationRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB document for vaccination recommendations
 */
@Document(collection = "vaccinationRecommendations")
public class VaccinationRecommendationDocument {
    
    @Id
    private String id;
    private String schemaVersion;
    private String source;
    private String generatedAtUtc;
    private String sourcePdf;
    private String sourceNote;
    private VaccinationRecommendationRequest.Units units;
    private List<VaccinationRecommendationRequest.VaccineRecommendation> vaccines;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public VaccinationRecommendationDocument() {}
    
    public VaccinationRecommendationDocument(VaccinationRecommendationRequest request) {
        this.schemaVersion = request.getSchemaVersion();
        this.generatedAtUtc = request.getGeneratedAtUtc();
        this.sourcePdf = request.getSourcePdf();
        this.sourceNote = request.getSourceNote();
        this.source = request.getSourcePdf(); // Set source from sourcePdf since source is not in the JSON
        this.units = request.getUnits();
        this.vaccines = request.getVaccines();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSchemaVersion() {
        return schemaVersion;
    }
    
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getGeneratedAtUtc() {
        return generatedAtUtc;
    }
    
    public void setGeneratedAtUtc(String generatedAtUtc) {
        this.generatedAtUtc = generatedAtUtc;
    }
    
    public String getSourcePdf() {
        return sourcePdf;
    }
    
    public void setSourcePdf(String sourcePdf) {
        this.sourcePdf = sourcePdf;
    }
    
    public String getSourceNote() {
        return sourceNote;
    }
    
    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }
    
    public VaccinationRecommendationRequest.Units getUnits() {
        return units;
    }
    
    public void setUnits(VaccinationRecommendationRequest.Units units) {
        this.units = units;
    }
    
    public List<VaccinationRecommendationRequest.VaccineRecommendation> getVaccines() {
        return vaccines;
    }
    
    public void setVaccines(List<VaccinationRecommendationRequest.VaccineRecommendation> vaccines) {
        this.vaccines = vaccines;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 