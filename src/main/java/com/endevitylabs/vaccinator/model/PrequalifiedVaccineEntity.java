package com.endevitylabs.vaccinator.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prequalified_vaccines")
public class PrequalifiedVaccineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_prequalification", nullable = false)
    private LocalDate dateOfPreQualification;

    @Column(name = "vaccine_type", nullable = false)
    private String vaccineType;

    @Column(name = "commercial_name", nullable = false)
    private String commercialName;

    @Column(name = "presentation", nullable = false)
    private String presentation;

    @Column(name = "number_of_doses")
    private Integer numberOfDoses;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "responsible_nra", nullable = false)
    private String responsibleNRA;

    // Default constructor
    public PrequalifiedVaccineEntity() {
    }

    // Constructor with all fields
    public PrequalifiedVaccineEntity(LocalDate dateOfPreQualification, String vaccineType,
                                     String commercialName, String presentation, Integer numberOfDoses,
                                     String manufacturer, String responsibleNRA) {
        this.dateOfPreQualification = dateOfPreQualification;
        this.vaccineType = vaccineType;
        this.commercialName = commercialName;
        this.presentation = presentation;
        this.numberOfDoses = numberOfDoses;
        this.manufacturer = manufacturer;
        this.responsibleNRA = responsibleNRA;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "PrequalifiedVaccineEntity{" +
                "id=" + id +
                ", dateOfPrequalification=" + dateOfPreQualification +
                ", vaccineType='" + vaccineType + '\'' +
                ", commercialName='" + commercialName + '\'' +
                ", presentation='" + presentation + '\'' +
                ", numberOfDoses=" + numberOfDoses +
                ", manufacturer='" + manufacturer + '\'' +
                ", responsibleNRA='" + responsibleNRA + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrequalifiedVaccineEntity that = (PrequalifiedVaccineEntity) o;

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