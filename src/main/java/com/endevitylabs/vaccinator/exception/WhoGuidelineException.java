package com.endevitylabs.vaccinator.exception;

/**
 * Custom exception for WHO guideline related errors
 */
public class WhoGuidelineException extends RuntimeException {
    
    public WhoGuidelineException(String message) {
        super(message);
    }
    
    public WhoGuidelineException(String message, Throwable cause) {
        super(message, cause);
    }
} 