
package com.example.service_registry.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service_registry.model.Movie;
import com.example.service_registry.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    // Add a new movie
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Movie movie) {
        try {
            if (movie == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Movie details are required"));
            }

            Movie savedMovie = service.save(movie);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedMovie);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return service.getAll();
    }

    // Get a movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Movie ID must be positive"));
        }

        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update a movie
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Movie movie) {

        if (id == null || id <= 0 || movie == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Valid movie ID and details are required"));
        }

        try {
            return ResponseEntity.ok(service.update(id, movie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete a movie
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Movie ID must be positive"));
        }

        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
