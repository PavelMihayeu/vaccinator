package com.endevitylabs.vaccinator.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "app.api.key=test-api-key-123",
    "app.api.key.header=X-API-Key"
})
class ApiKeyAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenValidApiKey_thenAccessGranted() throws Exception {
        mockMvc.perform(get("/api/v1/vaccines")
                .header("X-API-Key", "test-api-key-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenInvalidApiKey_thenAccessDenied() throws Exception {
        mockMvc.perform(get("/api/v1/vaccines")
                .header("X-API-Key", "invalid-key")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenNoApiKey_thenAccessDenied() throws Exception {
        mockMvc.perform(get("/api/v1/vaccines")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenSwaggerUi_thenAccessGranted() throws Exception {
        mockMvc.perform(get("/swagger-ui.html")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
} 