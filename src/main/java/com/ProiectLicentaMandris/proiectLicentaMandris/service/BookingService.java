package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Booking;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.BookingRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.ShowtimeRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    public List<Booking> getBookingByUserId(Long userId) {
        return bookingRepository.findByUserUserId(userId);
    }

    public List<Booking> createBooking(Long showtimeId, List<String> selectedSeats, Long userId) {
        List<Booking> bookings = new ArrayList<>();

        // Retrieve Showtime and User from the database
        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalShowtime.isPresent() && optionalUser.isPresent()) {
            Showtime showtime = optionalShowtime.get();
            User user = optionalUser.get();

            if (!selectedSeats.isEmpty()) {
                for (String seatNumber : selectedSeats) {
                    Booking booking = new Booking();
                    booking.setUser(user);
                    booking.setShowtime(showtime);
                    booking.setBookedSeats(seatNumber);
                    booking.setDate(LocalDateTime.now());
                    bookings.add(bookingRepository.save(booking));
                }
            }
        }
        return bookings;
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElse(null); // Or throw an exception if not found
    }

    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

}

