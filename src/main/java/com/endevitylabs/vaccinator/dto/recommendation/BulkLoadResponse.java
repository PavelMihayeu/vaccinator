package com.endevitylabs.vaccinator.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkLoadResponse {
    private String message;
    private long existingCount;
    private long newCount;
    private long failedCount;
    private List<String> failedVaccines;
    private String schemaVersion;
    private String source;
    private LocalDateTime uploadedAt;
    private LocalDateTime timestamp;
    private String apiVersion;
}
