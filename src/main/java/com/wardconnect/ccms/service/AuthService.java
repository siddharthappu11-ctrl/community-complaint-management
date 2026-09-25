package com.wardconnect.ccms.service;

import com.wardconnect.ccms.dto.AuthResponse;
import com.wardconnect.ccms.dto.LoginRequest;
import com.wardconnect.ccms.dto.RegisterRequest;

/**
 * OOP Concept: Interface & Abstraction
 * Interface declaring authentication service operations.
 */
public interface AuthService {
    AuthResponse registerResident(RegisterRequest request);
    AuthResponse loginResident(LoginRequest request);
    AuthResponse loginAdmin(LoginRequest request);
}
