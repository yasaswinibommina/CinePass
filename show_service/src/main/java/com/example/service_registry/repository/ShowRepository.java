package com.example.service_registry.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.service_registry.model.Show;

public interface ShowRepository
        extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByShowDate(LocalDate showDate);
}