package com.wardconnect.ccms.exception;

/**
 * OOP Concept: Exception Handling (Custom Exception)
 * Thrown when login authentication fails due to incorrect credentials.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
