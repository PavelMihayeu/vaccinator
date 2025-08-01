package com.endevitylabs.vaccinator.mapper;

import com.endevitylabs.vaccinator.dto.VaccineDataDto;
import com.endevitylabs.vaccinator.dto.PrequalifiedVaccineDto;
import com.endevitylabs.vaccinator.model.VaccineDocument;
import com.endevitylabs.vaccinator.model.PreQualifiedVaccine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VaccineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "schedules", source = "schedules")
    @Mapping(target = "prequalifiedVaccines", source = "prequalifiedVaccines")
    VaccineDocument toDocument(VaccineDataDto vaccineDataDto);

    @Mapping(target = "schedules", source = "schedules")
    @Mapping(target = "prequalifiedVaccines", source = "prequalifiedVaccines")
    VaccineDataDto toDto(VaccineDocument vaccineDocument);

    List<VaccineDataDto> toDto(List<VaccineDocument> vaccineDocumentList);

    // Map PrequalifiedVaccineDto to PreQualifiedVaccine
    PreQualifiedVaccine prequalifiedVaccineDtoToPreQualifiedVaccine(PrequalifiedVaccineDto prequalifiedVaccineDto);
    
    // Map PreQualifiedVaccine to PrequalifiedVaccineDto
    PrequalifiedVaccineDto toDto(PreQualifiedVaccine preQualifiedVaccine);
    
    List<PreQualifiedVaccine> prequalifiedVaccineDtoListToPreQualifiedVaccineList(List<PrequalifiedVaccineDto> prequalifiedVaccineDtoList);
    
    List<PrequalifiedVaccineDto> preQualifiedVaccineListToPrequalifiedVaccineDtoList(List<PreQualifiedVaccine> preQualifiedVaccineList);
} 