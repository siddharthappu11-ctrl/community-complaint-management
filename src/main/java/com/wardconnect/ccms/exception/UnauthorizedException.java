package com.wardconnect.ccms.exception;

/**
 * OOP Concept: Exception Handling (Custom Exception)
 * Thrown when an unauthenticated or unauthorized action is attempted.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
