package com.endevitylabs.vaccinator.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dose {
    private Integer dose;
    private String presentation;
    private Integer minAgeMonths;
    private Integer maxAgeMonths;
    private Integer fixedMonthsFromStart;
    private Integer minIntervalWeeksFromPrev;
    private List<String> notes;
    private List<String> rules;
}
