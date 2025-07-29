package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.Consideration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsiderationRepository extends JpaRepository<Consideration, Integer> {

    Optional<Consideration> findByName(String name);
} 