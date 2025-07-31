package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.Dose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoseRepository extends JpaRepository<Dose, UUID> {
    
    @Query("SELECT d FROM Dose d JOIN FETCH d.schedule s WHERE s.id = :scheduleId ORDER BY d.doseNumber")
    List<Dose> findByScheduleIdOrderByDoseNumber(@Param("scheduleId") UUID scheduleId);
    
    List<Dose> findByScheduleId(UUID scheduleId);
    
    @Query("SELECT d FROM Dose d WHERE d.schedule.id = :scheduleId AND d.doseNumber = :doseNumber")
    Optional<Dose> findByScheduleAndDoseNumber(@Param("scheduleId") UUID scheduleId, @Param("doseNumber") Integer doseNumber);
} 