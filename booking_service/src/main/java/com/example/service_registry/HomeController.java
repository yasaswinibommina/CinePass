package com.example.service_registry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "CinePass Booking Service is running!";
    }

    @GetMapping("/status")
    public String status() {
        return "Booking Service is UP";
    }
}