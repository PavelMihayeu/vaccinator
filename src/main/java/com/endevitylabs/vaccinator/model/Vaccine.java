package com.endevitylabs.vaccinator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NamedEntityGraph(
        name = "Vaccine.full",
        attributeNodes = {
                @NamedAttributeNode("targetGroups"),
                @NamedAttributeNode("regions"),
                @NamedAttributeNode("considerations"),
                @NamedAttributeNode(value = "schedules", subgraph = "schedulesWithDoses")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "schedulesWithDoses",
                        attributeNodes = @NamedAttributeNode("doses")
                )
        }
)
@Table(name = "vaccine")
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "type", length = 200)
    private String type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "who_reference_url", columnDefinition = "TEXT")
    private String whoReferenceUrl;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<VaccineSchedule> schedules = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "vaccine_target_group",
        joinColumns = @JoinColumn(name = "vaccine_id"),
        inverseJoinColumns = @JoinColumn(name = "age_group_id")
    )
    private Set<AgeGroup> targetGroups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "vaccine_region",
        joinColumns = @JoinColumn(name = "vaccine_id"),
        inverseJoinColumns = @JoinColumn(name = "region_id")
    )
    private Set<Region> regions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "vaccine_consideration",
        joinColumns = @JoinColumn(name = "vaccine_id"),
        inverseJoinColumns = @JoinColumn(name = "consideration_id")
    )
    private Set<Consideration> considerations = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public Set<VaccineSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<VaccineSchedule> schedules) {
        this.schedules = schedules;
    }

    public Set<AgeGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(Set<AgeGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    public Set<Consideration> getConsiderations() {
        return considerations;
    }

    public void setConsiderations(Set<Consideration> considerations) {
        this.considerations = considerations;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Vaccine vaccine = (Vaccine) object;
        return Objects.equals(id, vaccine.id) && Objects.equals(name, vaccine.name) && Objects.equals(type, vaccine.type) && Objects.equals(description, vaccine.description) && Objects.equals(whoReferenceUrl, vaccine.whoReferenceUrl) && Objects.equals(createdBy, vaccine.createdBy) && Objects.equals(updatedBy, vaccine.updatedBy) && Objects.equals(createdAt, vaccine.createdAt) && Objects.equals(updatedAt, vaccine.updatedAt) && Objects.equals(schedules, vaccine.schedules) && Objects.equals(targetGroups, vaccine.targetGroups) && Objects.equals(regions, vaccine.regions) && Objects.equals(considerations, vaccine.considerations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, description, whoReferenceUrl, createdBy, updatedBy, createdAt, updatedAt, schedules, targetGroups, regions, considerations);
    }
}