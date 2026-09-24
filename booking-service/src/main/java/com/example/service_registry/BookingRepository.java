package com.example.service_registry;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    boolean existsByShowIdAndSeatNumber(
            Long showId,
            String seatNumber
    );

    Optional<Booking> findByShowIdAndSeatNumber(
            Long showId,
            String seatNumber
    );

    List<Booking> findByShowId(Long showId);

    List<Booking> findByUserEmail(String userEmail);
}