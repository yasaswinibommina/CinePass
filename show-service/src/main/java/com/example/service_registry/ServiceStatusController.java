package com.example.service_registry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceStatusController {

    @GetMapping("/")
    public String home() {
        return "CinePass Show Service is running";
    }
}