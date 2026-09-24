package com.example.service_registry;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(
            @RequestBody Movie movie) {

        Movie savedMovie = movieRepository.save(movie);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMovie);
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(
            @PathVariable Long id) {

        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                    ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @RequestBody Movie movieDetails) {

        return movieRepository.findById(id)
                .map(movie -> {

                    movie.setTitle(
                        movieDetails.getTitle()
                    );

                    movie.setGenre(
                        movieDetails.getGenre()
                    );

                    movie.setLanguage(
                        movieDetails.getLanguage()
                    );

                    movie.setDurationMinutes(
                        movieDetails.getDurationMinutes()
                    );

                    movie.setDescription(
                        movieDetails.getDescription()
                    );

                    return ResponseEntity.ok(
                        movieRepository.save(movie)
                    );
                })
                .orElse(
                    ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id) {

        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        movieRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}