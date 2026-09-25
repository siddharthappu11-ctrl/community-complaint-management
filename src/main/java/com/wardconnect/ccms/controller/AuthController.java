package com.wardconnect.ccms.controller;

import com.wardconnect.ccms.dto.AuthResponse;
import com.wardconnect.ccms.dto.LoginRequest;
import com.wardconnect.ccms.dto.RegisterRequest;
import com.wardconnect.ccms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling public authentication endpoints for residents and admins.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/resident/register")
    public ResponseEntity<AuthResponse> registerResident(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerResident(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/resident/login")
    public ResponseEntity<AuthResponse> loginResident(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.loginResident(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> loginAdmin(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.loginAdmin(request);
        return ResponseEntity.ok(response);
    }
}
