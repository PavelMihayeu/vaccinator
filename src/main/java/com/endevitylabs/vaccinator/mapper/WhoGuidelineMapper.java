package com.endevitylabs.vaccinator.mapper;

import com.endevitylabs.vaccinator.dto.WhoGuidelineSummaryDto;
import com.endevitylabs.vaccinator.dto.WhoGuidelineTableDto;
import com.endevitylabs.vaccinator.model.WhoGuidelineSummary;
import com.endevitylabs.vaccinator.model.WhoGuidelineTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WhoGuidelineMapper {

    @Mapping(target = "tables", source = "tables")
    WhoGuidelineSummaryDto toDto(WhoGuidelineSummary summary);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "url", source = "url")
    WhoGuidelineTableDto toDto(WhoGuidelineTable table);

    List<WhoGuidelineTableDto> toDtoList(List<WhoGuidelineTable> tables);
} 