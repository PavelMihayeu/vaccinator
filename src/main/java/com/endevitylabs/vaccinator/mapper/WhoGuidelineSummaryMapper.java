package com.endevitylabs.vaccinator.mapper;

import com.endevitylabs.vaccinator.dto.WhoGuidelineSummary;
import com.endevitylabs.vaccinator.model.WhoGuidelineSummaryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WhoGuidelineSummaryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    WhoGuidelineSummaryDocument toDocument(WhoGuidelineSummary whoGuidelineSummary);

    WhoGuidelineSummary toDto(WhoGuidelineSummaryDocument whoGuidelineSummaryDocument);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateDocumentFromDto(WhoGuidelineSummary whoGuidelineSummary, @MappingTarget WhoGuidelineSummaryDocument whoGuidelineSummaryDocument);
} 