package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Booking;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.BookingRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.BookingService;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.ShowtimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("{id}")
    public ResponseEntity<List<Booking>> getBookingByUserId(@PathVariable("id") Long userId) {
        List<Booking> bookings = bookingService.getBookingByUserId(userId);
        if(bookings != null) {
            return ResponseEntity.ok(bookings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping()
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody Map<String, Object> bookingData) {
        Long showtimeId = Long.valueOf(bookingData.get("showtimeId").toString());
        List<String> selectedSeats = (List<String>) bookingData.get("selectedSeats");
        Long userId = Long.valueOf(bookingData.get("userId").toString());

        // Perform validation and create bookings
        List<Booking> bookings = bookingService.createBooking(showtimeId, selectedSeats, userId);

        // Update showtime seat availability using the service
        Showtime updatedShowtime = showtimeService.updateSeatAvailability(showtimeId, selectedSeats);

        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings);
        response.put("updatedShowtime", updatedShowtime);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable("id") Long bookingId) {
        try {
            // Retrieve the booking by ID
            Booking booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                return ResponseEntity.notFound().build();
            }

            // Delete the booking from the database
            bookingService.deleteBooking(bookingId);

            // Update the showtime's seat grid
            showtimeService.updateSeatAvailabilityForCanceledBooking(booking.getShowtime().getShowtimeId(), booking.getBookedSeats());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error canceling booking: " + e.getMessage());
        }
    }


}