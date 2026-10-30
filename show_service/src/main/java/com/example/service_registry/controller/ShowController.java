
package com.example.service_registry.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service_registry.model.Show;
import com.example.service_registry.service.ShowService;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService service;

    public ShowController(ShowService service) {
        this.service = service;
    }

    // Get all shows
    @GetMapping
    public List<Show> all() {
        return service.getAll();
    }

    // Get a show by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Show ID must be positive"));
        }

        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get shows for a particular movie
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> byMovie(@PathVariable Long movieId) {
        if (movieId == null || movieId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Movie ID must be positive"));
        }

        return ResponseEntity.ok(service.byMovie(movieId));
    }

    // Add a new show
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Show show) {
        try {
            if (show == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Show details are required"));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.save(show));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update a show
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Show show) {

        if (id == null || id <= 0 || show == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Valid show ID and details are required"));
        }

        try {
            return ResponseEntity.ok(service.update(id, show));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete a show
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Show ID must be positive"));
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
