
package com.example.service_registry.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.service_registry.model.User;
import com.example.service_registry.security.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getMyProfile(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }
}
