package com.wardconnect.ccms.exception;

/**
 * OOP Concept: Exception Handling (Custom Exception)
 * Thrown when a requested resource (User, Complaint) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
