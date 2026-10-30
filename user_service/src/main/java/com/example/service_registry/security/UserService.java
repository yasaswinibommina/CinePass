package com.example.service_registry.security;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.service_registry.model.User;
import com.example.service_registry.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}