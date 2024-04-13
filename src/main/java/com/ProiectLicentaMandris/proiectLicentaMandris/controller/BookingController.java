package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.*;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.BookingRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ShowtimeService showtimeService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;

    @GetMapping("{id}")
    public ResponseEntity<List<Booking>> getBookingByUserId(@PathVariable("id") Long userId) {
        List<Booking> bookings = bookingService.getBookingByUserId(userId);
        if(bookings != null) {
            return ResponseEntity.ok(bookings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
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

        // Retrieve showtime, movie, user, and cinema details
        Showtime showtime = showtimeService.getShowtimeById(showtimeId).orElse(null);
        Movie movie = showtime != null ? showtimeService.getMovieByShowtimeId(showtimeId).orElse(null) : null;
        User user = userService.getUserById(userId).orElse(null);
        Cinema cinema = showtime != null ? showtime.getCinema() : null;

        if (showtime == null || movie == null || user == null || cinema == null) {
            return ResponseEntity.notFound().build();
        }

        // Format the start time and end time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String formattedDate = showtime.getStartTime().format(formatter) + " to " + showtime.getEndTime().format(formatter);

        // Construct the email body
        String subject = "Booking Confirmation for " + movie.getTitle();
        String body = "\nYour booking for the movie \"" + movie.getTitle() + "\" on: " +
                "\nDate: " + formattedDate +
                "\nCinema: " + cinema.getName() + "-" + cinema.getLocation() +
                "\nYou have booked the following seats: \n";

        // Format selected seats with booking ID and movie ticket emoji
        for (String seat : selectedSeats) {
            String[] seatParts = seat.split("-");
            String formattedSeat = "Row " + seatParts[0] + " Seat " + seatParts[1] + " \uD83C\uDFAB"; // Unicode for ticket emoji
            body += formattedSeat + "\n";
        }

        // Add booking number to the email body
        body += "Booking Number: " + bookings.get(0).getBookingId();

        // Send booking confirmation email to the user
        emailSenderService.sendEmail(user.getEmail(), subject, body);

        // Prepare the response
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