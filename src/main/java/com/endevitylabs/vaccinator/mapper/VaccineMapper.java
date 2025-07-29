package com.endevitylabs.vaccinator.mapper;

import com.endevitylabs.vaccinator.dto.*;
import com.endevitylabs.vaccinator.model.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VaccineMapper {

    @Mapping(target = "targetGroups", source = "targetGroups", qualifiedByName = "ageGroupsToStrings")
    @Mapping(target = "regions", source = "regions", qualifiedByName = "regionsToStrings")
    @Mapping(target = "considerations", source = "considerations", qualifiedByName = "considerationsToStrings")
    @Mapping(target = "schedules", source = "schedules")
    VaccineDto toDto(Vaccine vaccine);

    @Mapping(target = "targetGroups", ignore = true)
    @Mapping(target = "regions", ignore = true)
    @Mapping(target = "considerations", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vaccine toEntity(CreateVaccineRequest request);

    @Mapping(target = "vaccine", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VaccineSchedule toEntity(CreateVaccineScheduleRequest request);

    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "id", ignore = true)
    Dose toEntity(CreateDoseRequest request);

    VaccineScheduleDto toDto(VaccineSchedule schedule);
    DoseDto toDto(Dose dose);

    List<VaccineScheduleDto> toScheduleDtoList(List<VaccineSchedule> schedules);
    List<DoseDto> toDoseDtoList(List<Dose> doses);

    @Named("ageGroupsToStrings")
    default Set<String> ageGroupsToStrings(Set<AgeGroup> ageGroups) {
        if (ageGroups == null) return null;
        return ageGroups.stream()
                .map(AgeGroup::getName)
                .collect(Collectors.toSet());
    }

    @Named("regionsToStrings")
    default Set<String> regionsToStrings(Set<Region> regions) {
        if (regions == null) return null;
        return regions.stream()
                .map(Region::getName)
                .collect(Collectors.toSet());
    }

    @Named("considerationsToStrings")
    default Set<String> considerationsToStrings(Set<Consideration> considerations) {
        if (considerations == null) return null;
        return considerations.stream()
                .map(Consideration::getName)
                .collect(Collectors.toSet());
    }
} 