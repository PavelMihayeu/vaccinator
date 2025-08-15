package com.endevitylabs.vaccinator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiVersionConfig {
    
    @Value("${app.api.version:1.0}")
    private String apiVersion;
    
    public String getApiVersion() {
        return apiVersion;
    }
}
