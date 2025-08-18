package com.endevitylabs.vaccinator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "prequalified_vaccines")
public class PrequalifiedVaccineDocument {

    @Id
    private String id;

    @Field("date_of_prequalification")
    private LocalDate dateOfPreQualification;

    @Field("vaccine_type")
    private String vaccineType;

    @Field("commercial_name")
    private String commercialName;

    @Field("presentation")
    private String presentation;

    @Field("number_of_doses")
    private Integer numberOfDoses;

    @Field("manufacturer")
    private String manufacturer;

    @Field("responsible_nra")
    private String responsibleNRA;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public PrequalifiedVaccineDocument() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with all fields
    public PrequalifiedVaccineDocument(LocalDate dateOfPreQualification, String vaccineType,
                                       String commercialName, String presentation, Integer numberOfDoses,
                                       String manufacturer, String responsibleNRA) {
        this();
        this.dateOfPreQualification = dateOfPreQualification;
        this.vaccineType = vaccineType;
        this.commercialName = commercialName;
        this.presentation = presentation;
        this.numberOfDoses = numberOfDoses;
        this.manufacturer = manufacturer;
        this.responsibleNRA = responsibleNRA;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateOfPreQualification() {
        return dateOfPreQualification;
    }

    public void setDateOfPreQualification(LocalDate dateOfPreQualification) {
        this.dateOfPreQualification = dateOfPreQualification;
    }

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public Integer getNumberOfDoses() {
        return numberOfDoses;
    }

    public void setNumberOfDoses(Integer numberOfDoses) {
        this.numberOfDoses = numberOfDoses;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getResponsibleNRA() {
        return responsibleNRA;
    }

    public void setResponsibleNRA(String responsibleNRA) {
        this.responsibleNRA = responsibleNRA;
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

    @Override
    public String toString() {
        return "PrequalifiedVaccineDocument{" +
                "id='" + id + '\'' +
                ", dateOfPreQualification=" + dateOfPreQualification +
                ", vaccineType='" + vaccineType + '\'' +
                ", commercialName='" + commercialName + '\'' +
                ", presentation='" + presentation + '\'' +
                ", numberOfDoses=" + numberOfDoses +
                ", manufacturer='" + manufacturer + '\'' +
                ", responsibleNRA='" + responsibleNRA + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrequalifiedVaccineDocument that = (PrequalifiedVaccineDocument) o;

        if (!dateOfPreQualification.equals(that.dateOfPreQualification)) return false;
        if (!vaccineType.equals(that.vaccineType)) return false;
        if (!commercialName.equals(that.commercialName)) return false;
        if (!presentation.equals(that.presentation)) return false;
        if (numberOfDoses != null ? !numberOfDoses.equals(that.numberOfDoses) : that.numberOfDoses != null) return false;
        if (!manufacturer.equals(that.manufacturer)) return false;
        return responsibleNRA.equals(that.responsibleNRA);
    }

    @Override
    public int hashCode() {
        int result = dateOfPreQualification.hashCode();
        result = 31 * result + vaccineType.hashCode();
        result = 31 * result + commercialName.hashCode();
        result = 31 * result + presentation.hashCode();
        result = 31 * result + (numberOfDoses != null ? numberOfDoses.hashCode() : 0);
        result = 31 * result + manufacturer.hashCode();
        result = 31 * result + responsibleNRA.hashCode();
        return result;
    }
}
