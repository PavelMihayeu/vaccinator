package com.endevitylabs.vaccinator.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register JavaTimeModule for proper LocalDateTime handling
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        mapper.registerModule(javaTimeModule);
        
        // Configure to write dates as ISO strings instead of arrays
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Create a module for handling NaN values
        SimpleModule nanModule = new SimpleModule();
        nanModule.addDeserializer(Double.class, new DoubleDeserializer());
        nanModule.addDeserializer(Float.class, new FloatDeserializer());
        mapper.registerModule(nanModule);
        
        return mapper;
    }
    
    public static class DoubleDeserializer extends JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            if ("NaN".equals(value) || "null".equals(value)) {
                return null;
            }
            return p.getDoubleValue();
        }
    }
    
    public static class FloatDeserializer extends JsonDeserializer<Float> {
        @Override
        public Float deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            if ("NaN".equals(value) || "null".equals(value)) {
                return null;
            }
            return p.getFloatValue();
        }
    }
} 