package com.endevitylabs.vaccinator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vaccinator API")
                        .description("""
                                API for managing WHO vaccination guidelines and recommendations.
                                
                                ## API Versions
                                - **v1**: Current stable version with basic vaccine data endpoints
                                - **v2**: Enhanced version with improved response format and metadata
                                
                                ## Endpoints
                                - `/api/v1/vaccines` - Public vaccine data endpoints
                                - `/api/v1/admin/data` - Admin data management endpoints
                                - `/api/v2/vaccines` - Enhanced vaccine endpoints (V2)
                                
                                ## Authentication
                                - Public endpoints: No authentication required
                                - Admin endpoints: API key authentication (currently disabled in dev)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vaccinator Team")
                                .email("support@vaccinator.com")
                                .url("https://vaccinator.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.vaccinator.com")
                                .description("Production Server")
                ));
    }
} 