package com.endevitylabs.vaccinator.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
    "app.api.version=2.0"
})
class ApiVersionConfigTest {

    @Autowired
    private ApiVersionConfig apiVersionConfig;

    @Test
    void testApiVersionFromProperties() {
        assertEquals("2.0", apiVersionConfig.getApiVersion());
    }
}
