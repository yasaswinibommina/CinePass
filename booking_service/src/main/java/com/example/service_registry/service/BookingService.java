
package com.example.service_registry.service;

import java.util.List;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.service_registry.model.Booking;
import com.example.service_registry.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Booking book(
            Long showId,
            String seatNumber,
            String userEmail) {

        if (showId == null || showId <= 0) {
            throw new IllegalArgumentException(
                    "Show ID must be a positive number");
        }

        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Seat number is required");
        }

        seatNumber = seatNumber.trim().toUpperCase(Locale.ROOT);

        if (!seatNumber.matches("[A-Z][0-9]{1,3}")) {
            throw new IllegalArgumentException(
                    "Seat number must look like A1, B12, or C100");
        }

        if (userEmail == null || userEmail.isBlank()
                || !userEmail.trim().matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException(
                    "A valid user email is required");
        }

        userEmail = userEmail.trim().toLowerCase(Locale.ROOT);

        if (repository.existsByShowIdAndSeatNumber(
                showId, seatNumber)) {
            throw new IllegalStateException(
                    "Seat already booked");
        }

        Booking booking = new Booking();
        booking.setShowId(showId);
        booking.setSeatNumber(seatNumber);
        booking.setUserEmail(userEmail);
        booking.setStatus("CONFIRMED");

        try {
            return repository.saveAndFlush(booking);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Seat already booked");
        }
    }

    public List<Booking> all() {
        return repository.findAll();
    }

    public List<Booking> byShow(Long showId) {
        return repository.findByShowId(showId);
    }

    public List<Booking> byUser(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required");
        }

        return repository.findByUserEmail(
                email.trim().toLowerCase(Locale.ROOT));
    }
}
