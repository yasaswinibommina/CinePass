package com.example.service_registry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.service_registry.model.Movie;

public interface MovieRepository
        extends JpaRepository<Movie, Long> {
}