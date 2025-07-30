package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.VaccineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccineScheduleRepository extends JpaRepository<VaccineSchedule, UUID> {
    
    @Query("SELECT vs FROM VaccineSchedule vs JOIN FETCH vs.vaccine v JOIN FETCH vs.doses WHERE vs.vaccine.id = :vaccineId")
    List<VaccineSchedule> findByVaccineIdWithDetails(@Param("vaccineId") UUID vaccineId);
    
    List<VaccineSchedule> findByVaccineId(UUID vaccineId);
} 