package com.endevitylabs.vaccinator.model;

import com.endevitylabs.vaccinator.dto.ScheduleData;
import com.endevitylabs.vaccinator.dto.VaccineSchedule;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "vaccines")
public class VaccineDocument {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("type")
    private String type;

    @Field("description")
    private String description;

    @Field("who_reference_url")
    private String whoReferenceUrl;

    @Field("target_groups")
    private List<String> targetGroups;

    @Field("regions")
    private List<String> regions;

    @Field("considerations")
    private List<String> considerations;

    @Field("schedules")
    private List<com.endevitylabs.vaccinator.dto.ScheduleData> schedules;

    @Field("prequalified_vaccines")
    private List<PreQualifiedVaccine> prequalifiedVaccines;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWhoReferenceUrl() {
        return whoReferenceUrl;
    }

    public void setWhoReferenceUrl(String whoReferenceUrl) {
        this.whoReferenceUrl = whoReferenceUrl;
    }

    public List<String> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<String> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getConsiderations() {
        return considerations;
    }

    public void setConsiderations(List<String> considerations) {
        this.considerations = considerations;
    }

    public List<com.endevitylabs.vaccinator.dto.ScheduleData> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<com.endevitylabs.vaccinator.dto.ScheduleData> schedules) {
        this.schedules = schedules;
    }

    public List<PreQualifiedVaccine> getPrequalifiedVaccines() {
        return prequalifiedVaccines;
    }

    public void setPrequalifiedVaccines(List<PreQualifiedVaccine> prequalifiedVaccines) {
        this.prequalifiedVaccines = prequalifiedVaccines;
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