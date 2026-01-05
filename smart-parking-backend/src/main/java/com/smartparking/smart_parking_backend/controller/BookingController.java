package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.dto.BookingRequestDTO;
import com.smartparking.smart_parking_backend.dto.BookingResponseDTO;
import com.smartparking.smart_parking_backend.service.BookingService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // CREATE BOOKING
    @PostMapping
    public BookingResponseDTO bookSlot(
            @Valid @RequestBody BookingRequestDTO dto) {
        return service.bookSlot(dto);
    }

    // GET BOOKING HISTORY
    @GetMapping("/me")
    public List<BookingResponseDTO> getMyBookings() {
        return service.getMyBookings();
    }

    // GET OWNER INCOMING BOOKINGS
    @GetMapping("/owner")
    public List<BookingResponseDTO> getOwnerBookings() {
        return service.getOwnerBookings();
    }

    @PostMapping("/{bookingId}/end")
    public BookingResponseDTO endBooking(@PathVariable Long bookingId) {
        return service.endBooking(bookingId);
    }

    @PostMapping("/{bookingId}/pay")
    public BookingResponseDTO payBooking(@PathVariable Long bookingId) {
        return service.processPayment(bookingId);
    }
}
