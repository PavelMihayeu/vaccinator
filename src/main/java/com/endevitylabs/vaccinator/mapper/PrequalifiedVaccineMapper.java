package com.endevitylabs.vaccinator.mapper;

import com.endevitylabs.vaccinator.dto.prequalified.PrequalifiedVaccineDto;
import com.endevitylabs.vaccinator.model.PrequalifiedVaccineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for PrequalifiedVaccineEntity and PrequalifiedVaccineDto
 */
@Mapper(componentModel = "spring")
public interface PrequalifiedVaccineMapper {

    /**
     * Convert entity to DTO (excluding id)
     */
    PrequalifiedVaccineDto toDto(PrequalifiedVaccineEntity entity);

    /**
     * Convert DTO to entity (id will be null for new entities)
     */
    @Mapping(target = "id", ignore = true)
    PrequalifiedVaccineEntity toEntity(PrequalifiedVaccineDto dto);
} 