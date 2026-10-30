
package com.example.service_registry.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.service_registry.model.Show;
import com.example.service_registry.repository.ShowRepository;

@Service
public class ShowService {

    private final ShowRepository repository;

    public ShowService(ShowRepository repository) {
        this.repository = repository;
    }

    // Get all shows
    public List<Show> getAll() {
        return repository.findAll();
    }

    // Get a show by ID
    public Show getById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid show ID");
        }

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Show not found with ID: " + id));
    }

    // Get shows by movie ID
    public List<Show> byMovie(Long movieId) {
        if (movieId == null || movieId <= 0) {
            throw new IllegalArgumentException("Invalid movie ID");
        }

        return repository.findByMovieId(movieId);
    }

    // Add a new show
    public Show save(Show show) {
        validateShow(show);
        return repository.save(show);
    }

    // Update an existing show
    @Transactional
    public Show update(Long id, Show input) {
        if (input == null) {
            throw new IllegalArgumentException("Show details are required");
        }

        Show existing = getById(id);
        validateShow(input);

        existing.setMovieId(input.getMovieId());
        existing.setScreenNumber(input.getScreenNumber());
        existing.setShowDate(input.getShowDate());
        existing.setStartTime(input.getStartTime());
        existing.setEndTime(input.getEndTime());

        return repository.save(existing);
    }

    // Delete a show
    public void delete(Long id) {
        Show existing = getById(id);
        repository.delete(existing);
    }

    // Validate show details
    private void validateShow(Show show) {
        if (show == null) {
            throw new IllegalArgumentException("Show details are required");
        }

        if (show.getMovieId() == null || show.getMovieId() <= 0) {
            throw new IllegalArgumentException("Valid movie ID is required");
        }

        if (show.getScreenNumber() <= 0) {
            throw new IllegalArgumentException(
                    "Screen number must be positive");
        }

        if (show.getShowDate() == null) {
            throw new IllegalArgumentException("Show date is required");
        }

        if (show.getStartTime() == null || show.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required");
        }

        if (!show.getEndTime().isAfter(show.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time");
        }
    }
}
