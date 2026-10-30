
package com.example.service_registry.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.service_registry.model.Booking;
import com.example.service_registry.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> book(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        try {
            String showIdValue = body.get("showId");
            String seatNumber = body.get("seatNumber");

            if (showIdValue == null || seatNumber == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "error",
                                "showId and seatNumber are required"));
            }

            Long showId = Long.parseLong(showIdValue);

            Booking booking = service.book(
                    showId,
                    seatNumber,
                    authentication.getName());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(booking);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Booking> myBookings(
            Authentication authentication) {
        return service.byUser(authentication.getName());
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<?> byShow(
            @PathVariable Long showId,
            Authentication authentication) {

        List<Booking> bookings = service.byShow(showId);

        List<Booking> myBookings = bookings.stream()
                .filter(booking -> booking.getUserEmail()
                        .equalsIgnoreCase(authentication.getName()))
                .toList();

        return ResponseEntity.ok(myBookings);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> byUser(
            @PathVariable String email,
            Authentication authentication) {

        if (!email.equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "You can view only your own bookings"));
        }

        return ResponseEntity.ok(service.byUser(email));
    }
}
