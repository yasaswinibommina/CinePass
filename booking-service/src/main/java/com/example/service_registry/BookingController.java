package com.example.service_registry;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(
            BookingRepository bookingRepository) {

        this.bookingRepository = bookingRepository;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request,
            Authentication authentication) {

        if (bookingRepository.existsByShowIdAndSeatNumber(
                request.showId(),
                request.seatNumber())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                        "message",
                        "Seat already booked"
                    ));
        }

        Booking booking = new Booking();

        booking.setUserEmail(
                authentication.getName()
        );

        booking.setShowId(
                request.showId()
        );

        booking.setSeatNumber(
                request.seatNumber()
        );

        booking.setStatus("CONFIRMED");

        Booking saved =
                bookingRepository.save(booking);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(
            @PathVariable Long id) {

        return bookingRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                    ResponseEntity.notFound().build()
                );
    }

    @GetMapping("/shows/{showId}")
    public List<Booking> getShowBookings(
            @PathVariable Long showId) {

        return bookingRepository
                .findByShowId(showId);
    }

    @GetMapping("/my")
    public List<Booking> getMyBookings(
            Authentication authentication) {

        return bookingRepository
                .findByUserEmail(authentication.getName());
    }

    public record BookingRequest(
            Long showId,
            String seatNumber
    ) {}
}