package com.example.service_registry.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.service_registry.model.Booking;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    boolean existsByShowIdAndSeatNumber(
        Long showId, String seatNumber
    );

    List<Booking> findByShowId(Long showId);

    List<Booking> findByUserEmail(String userEmail);
}