package com.wardconnect.ccms.exception;

/**
 * OOP Concept: Exception Handling (Custom Exception)
 * Thrown when trying to register with an email that already exists.
 */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
