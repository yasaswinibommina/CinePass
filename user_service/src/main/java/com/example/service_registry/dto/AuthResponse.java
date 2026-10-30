package com.example.service_registry.dto;

public class AuthResponse {
    private String message;
    private String token;
    private String email;

    public AuthResponse(String message, String token, String email) {
        this.message = message;
        this.token = token;
        this.email = email;
    }

    public String getMessage() { return message; }
    public String getToken() { return token; }
    public String getEmail() { return email; }
}