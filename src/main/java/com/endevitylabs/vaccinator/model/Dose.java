package com.endevitylabs.vaccinator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "dose")
public class Dose {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private VaccineSchedule schedule;

    @Column(name = "dose_number", nullable = false)
    private Integer doseNumber;

    @Column(name = "min_age", length = 50)
    private String minAge;

    @Column(name = "is_booster")
    private Boolean isBooster = false;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VaccineSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(VaccineSchedule schedule) {
        this.schedule = schedule;
    }

    public Integer getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public Boolean getBooster() {
        return isBooster;
    }

    public void setBooster(Boolean booster) {
        isBooster = booster;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dose dose = (Dose) object;
        return Objects.equals(id, dose.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}