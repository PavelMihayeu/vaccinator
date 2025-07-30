package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.Vaccine;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Hidden
@Repository
public interface VaccineRepository extends CrudRepository<Vaccine, UUID> {

    Optional<Vaccine> findByName(@Param("name") String name);

    @Query("SELECT v FROM Vaccine v JOIN FETCH v.targetGroups JOIN FETCH v.regions JOIN FETCH v.considerations JOIN FETCH v.schedules s JOIN FETCH s.doses")
    List<Vaccine> findAllWithDetails();

    @Query("SELECT v FROM Vaccine v JOIN FETCH v.targetGroups JOIN FETCH v.regions JOIN FETCH v.considerations JOIN FETCH v.schedules s JOIN FETCH s.doses WHERE v.id = :id")
    Optional<Vaccine> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT v FROM Vaccine v JOIN FETCH v.targetGroups JOIN FETCH v.regions JOIN FETCH v.considerations JOIN FETCH v.schedules s JOIN FETCH s.doses WHERE v.name ILIKE %:name%")
    List<Vaccine> findByNameContainingIgnoreCase(@Param("name") String name);
} 