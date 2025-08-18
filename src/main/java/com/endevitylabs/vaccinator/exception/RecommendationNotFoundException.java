package com.endevitylabs.vaccinator.exception;

/**
 * Exception thrown when vaccination recommendations are not found
 */
public class RecommendationNotFoundException extends RuntimeException {
    
    public RecommendationNotFoundException(String message) {
        super(message);
    }
    
    public RecommendationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}


