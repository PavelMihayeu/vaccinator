package com.endevitylabs.vaccinator.mapper;

import com.endevitylabs.vaccinator.dto.prequalified.PrequalifiedVaccineDto;
import com.endevitylabs.vaccinator.model.PrequalifiedVaccineDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for PrequalifiedVaccineDocument and PrequalifiedVaccineDto
 */
@Mapper(componentModel = "spring")
public interface PrequalifiedVaccineMapper {

    /**
     * Convert document to DTO (excluding id)
     */
    PrequalifiedVaccineDto toDto(PrequalifiedVaccineDocument document);

    /**
     * Convert DTO to document (id will be null for new documents)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PrequalifiedVaccineDocument toDocument(PrequalifiedVaccineDto dto);
} 