package com.wardconnect.ccms.service;

import com.wardconnect.ccms.dto.AuthResponse;
import com.wardconnect.ccms.dto.LoginRequest;
import com.wardconnect.ccms.dto.RegisterRequest;
import com.wardconnect.ccms.enums.Role;
import com.wardconnect.ccms.exception.DuplicateEmailException;
import com.wardconnect.ccms.exception.InvalidCredentialsException;
import com.wardconnect.ccms.model.Resident;
import com.wardconnect.ccms.model.User;
import com.wardconnect.ccms.repository.UserRepository;
import com.wardconnect.ccms.security.JwtTokenProvider;
import com.wardconnect.ccms.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider;
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Resident resident;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "ccms_secret_key_for_jwt_token_generation_wardconnect_2026_secure");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", 86400000L);

        authService = new AuthServiceImpl(userRepository, passwordEncoder, tokenProvider);

        registerRequest = new RegisterRequest("Test Resident", "resident@test.com", "password123");
        loginRequest = new LoginRequest("resident@test.com", "password123");
        resident = new Resident("user-1", "Test Resident", "resident@test.com", "hashed_pass");
    }

    @Test
    void testRegisterResident_Success() {
        when(userRepository.existsByEmail("resident@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_pass");
        when(userRepository.save(any(User.class))).thenReturn(resident);

        AuthResponse response = authService.registerResident(registerRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("Test Resident", response.getName());
        assertEquals(Role.RESIDENT, response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterResident_DuplicateEmailThrowsException() {
        when(userRepository.existsByEmail("resident@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.registerResident(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginResident_Success() {
        when(userRepository.findByEmail("resident@test.com")).thenReturn(Optional.of(resident));
        when(passwordEncoder.matches("password123", "hashed_pass")).thenReturn(true);

        AuthResponse response = authService.loginResident(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("Test Resident", response.getName());
    }

    @Test
    void testLoginResident_InvalidPasswordThrowsException() {
        when(userRepository.findByEmail("resident@test.com")).thenReturn(Optional.of(resident));
        when(passwordEncoder.matches("wrongpassword", "hashed_pass")).thenReturn(false);

        LoginRequest wrongLogin = new LoginRequest("resident@test.com", "wrongpassword");
        assertThrows(InvalidCredentialsException.class, () -> authService.loginResident(wrongLogin));
    }
}
