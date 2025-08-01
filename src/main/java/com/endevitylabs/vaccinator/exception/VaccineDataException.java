package com.endevitylabs.vaccinator.exception;

/**
 * Custom exception for vaccine data loading and processing errors
 */
public class VaccineDataException extends RuntimeException {
    
    public VaccineDataException(String message) {
        super(message);
    }
    
    public VaccineDataException(String message, Throwable cause) {
        super(message, cause);
    }
} 