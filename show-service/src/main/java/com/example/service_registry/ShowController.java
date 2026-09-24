package com.example.service_registry;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowRepository showRepository;

    public ShowController(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    // CREATE SHOW
    @PostMapping
    public ResponseEntity<Show> createShow(
            @RequestBody Show show) {

        Show savedShow = showRepository.save(show);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedShow);
    }

    // GET ALL SHOWS
    @GetMapping
    public List<Show> getAllShows() {

        return showRepository.findAll();
    }

    // GET SHOW BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Show> getShow(
            @PathVariable Long id) {

        return showRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                    ResponseEntity.notFound().build()
                );
    }

    // UPDATE SHOW
    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(
            @PathVariable Long id,
            @RequestBody Show showDetails) {

        return showRepository.findById(id)
                .map(show -> {

                    show.setMovieId(
                        showDetails.getMovieId()
                    );

                    show.setScreenNumber(
                        showDetails.getScreenNumber()
                    );

                    show.setShowDate(
                        showDetails.getShowDate()
                    );

                    show.setStartTime(
                        showDetails.getStartTime()
                    );

                    show.setEndTime(
                        showDetails.getEndTime()
                    );

                    return ResponseEntity.ok(
                        showRepository.save(show)
                    );
                })
                .orElse(
                    ResponseEntity.notFound().build()
                );
    }

    // DELETE SHOW
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long id) {

        if (!showRepository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        showRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}