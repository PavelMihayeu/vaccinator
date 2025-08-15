package com.endevitylabs.vaccinator.service;

import com.endevitylabs.vaccinator.config.ApiVersionConfig;
import com.endevitylabs.vaccinator.dto.recommendation.BulkLoadResponse;
import com.endevitylabs.vaccinator.dto.recommendation.VaccinationRecommendationRequest;
import com.endevitylabs.vaccinator.model.VaccinationRecommendationDocument;
import com.endevitylabs.vaccinator.repository.VaccinationRecommendationMongoRepository;
import com.endevitylabs.vaccinator.service.impl.VaccinationRecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "app.api.version=2.0"
})
class VaccinationRecommendationServiceTest {

    @Mock
    private VaccinationRecommendationMongoRepository repository;

    @Mock
    private ApiVersionConfig apiVersionConfig;

    private VaccinationRecommendationServiceImpl service;

    @BeforeEach
    void setUp() {
        when(apiVersionConfig.getApiVersion()).thenReturn("2.0");
        service = new VaccinationRecommendationServiceImpl(repository, apiVersionConfig);
    }

    @Test
    void testBulkLoadRecommendationsUsesDynamicApiVersion() {
        // Given
        VaccinationRecommendationRequest request = new VaccinationRecommendationRequest();
        request.setVaccines(new ArrayList<>());
        request.setSchemaVersion("1.0");
        request.setSourcePdf("test.pdf");

        when(repository.count()).thenReturn(0L);
        when(repository.save(any(VaccinationRecommendationDocument.class)))
                .thenReturn(new VaccinationRecommendationDocument(request));

        // When
        BulkLoadResponse response = service.bulkLoadRecommendations(request);

        // Then
        assertEquals("2.0", response.getApiVersion());
    }
}
