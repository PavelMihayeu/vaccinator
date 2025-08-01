package com.endevitylabs.vaccinator.repository;

import com.endevitylabs.vaccinator.model.VaccineDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaccineMongoRepository extends MongoRepository<VaccineDocument, String> {
    
    Optional<VaccineDocument> findByName(String name);
    
    List<VaccineDocument> findByNameContainingIgnoreCase(String name);
    
    List<VaccineDocument> findByType(String type);
} 