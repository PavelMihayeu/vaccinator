package com.endevitylabs.vaccinator.exception;

/**
 * Custom exception for cache-related errors
 */
public class CacheException extends RuntimeException {
    
    public CacheException(String message) {
        super(message);
    }
    
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
} 