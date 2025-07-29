package com.endevitylabs.vaccinator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "dose")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "schedule")
public class Dose {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
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
} 