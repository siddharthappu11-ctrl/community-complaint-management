package com.wardconnect.ccms.service.impl;

import com.wardconnect.ccms.dto.AuthResponse;
import com.wardconnect.ccms.dto.LoginRequest;
import com.wardconnect.ccms.dto.RegisterRequest;
import com.wardconnect.ccms.enums.Role;
import com.wardconnect.ccms.exception.DuplicateEmailException;
import com.wardconnect.ccms.exception.InvalidCredentialsException;
import com.wardconnect.ccms.model.Admin;
import com.wardconnect.ccms.model.Resident;
import com.wardconnect.ccms.model.User;
import com.wardconnect.ccms.repository.UserRepository;
import com.wardconnect.ccms.security.JwtTokenProvider;
import com.wardconnect.ccms.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * OOP Concept: Interface Implementation & Polymorphism
 * Implementation of AuthService.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse registerResident(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateEmailException("An account with this email already exists.");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());
        Resident resident = new Resident(null, request.getName().trim(), normalizedEmail, passwordHash);
        User savedUser = userRepository.save(resident);

        String token = tokenProvider.generateToken(savedUser);
        return new AuthResponse(token, savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    @Override
    public AuthResponse loginResident(LoginRequest request) {
        User user = authenticateUser(request.getEmail(), request.getPassword());
        if (user.getRole() != Role.RESIDENT) {
            throw new InvalidCredentialsException("Please use the admin login portal for admin accounts.");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public AuthResponse loginAdmin(LoginRequest request) {
        User user = authenticateUser(request.getEmail(), request.getPassword());
        if (user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("Resident accounts cannot access the admin portal.");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole());
    }

    private User authenticateUser(String email, String rawPassword) {
        String normalizedEmail = email.trim().toLowerCase();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException("Incorrect email or password."));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Incorrect email or password.");
        }
        return user;
    }
}
