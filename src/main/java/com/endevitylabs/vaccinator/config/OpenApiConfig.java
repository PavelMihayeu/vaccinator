package com.endevitylabs.vaccinator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
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
                        .version("1.0")
                        .description("""
                                Vaccine management API with MongoDB storage:
                                
                                ## Key Features:
                                - **Flexible JSON Storage**: Stores vaccine data as flexible JSON documents
                                - **Prequalified Vaccines**: Support for WHO prequalified vaccine information
                                - **NaN Handling**: Robust handling of NaN values in JSON data
                                - **ISO DateTime Format**: Proper datetime serialization
                                
                                ## API Features:
                                - **Flexible Schema**: No rigid structure constraints
                                - **Bulk Loading**: Load large datasets with NaN handling
                                - **CRUD Operations**: Full create, read, update, delete support
                                """)
                        .contact(new Contact()
                                .name("Endevity Labs")
                                .email("support@endevitylabs.com")
                                .url("https://endevitylabs.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("http://3.74.63.99")
                                .description("Stage server"),
                        new Server()
                                .url("https://api.vaccinator.com")
                                .description("Production server")
                ));
    }
} 