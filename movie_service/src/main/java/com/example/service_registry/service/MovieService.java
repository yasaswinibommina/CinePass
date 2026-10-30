
package com.example.service_registry.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.service_registry.model.Movie;
import com.example.service_registry.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    // Get all movies
    public List<Movie> getAll() {
        return repository.findAll();
    }

    // Get a movie by ID
    public Movie getById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid movie ID");
        }

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Movie not found with ID: " + id));
    }

    // Add a new movie
    public Movie save(Movie movie) {
        validateMovie(movie);
        return repository.save(movie);
    }

    // Update an existing movie
    @Transactional
    public Movie update(Long id, Movie input) {
        if (input == null) {
            throw new IllegalArgumentException("Movie details are required");
        }

        Movie movie = getById(id);

        validateMovie(input);

        movie.setTitle(input.getTitle().trim());
        movie.setGenre(input.getGenre().trim());
        movie.setDuration(input.getDuration());
        movie.setLanguage(input.getLanguage().trim());

        return repository.save(movie);
    }

    // Delete a movie
    public void delete(Long id) {
        Movie movie = getById(id);
        repository.delete(movie);
    }

    // Validate movie details
    private void validateMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie details are required");
        }

        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Movie title is required");
        }

        if (movie.getGenre() == null || movie.getGenre().isBlank()) {
            throw new IllegalArgumentException("Movie genre is required");
        }

        if (movie.getLanguage() == null || movie.getLanguage().isBlank()) {
            throw new IllegalArgumentException("Movie language is required");
        }

        if (movie.getDuration() <= 0) {
            throw new IllegalArgumentException("Movie duration must be positive");
        }
    }
}
