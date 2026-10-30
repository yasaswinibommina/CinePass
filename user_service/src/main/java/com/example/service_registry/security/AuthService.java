package com.example.service_registry.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.service_registry.dto.*;
import com.example.service_registry.model.User;
import com.example.service_registry.repository.UserRepository;
import com.example.service_registry.security.JwtService;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder encoder,
                       JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(
                request.getName(),
                email,
                encoder.encode(request.getPassword()));

        repository.save(user);

        return new AuthResponse(
                "Registration successful",
                jwtService.generateToken(email),
                email);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new AuthResponse(
                "Login successful",
                jwtService.generateToken(email),
                email);
    }
}