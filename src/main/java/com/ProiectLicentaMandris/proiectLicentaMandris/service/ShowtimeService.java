package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShowtimeService {
    @Autowired
    ShowtimeRepository showtimeRepository;

    public List<Showtime> getAllShowTimes() {
        return showtimeRepository.findAll();
    }
    public List<Showtime> getShowTimesByDate(LocalDate date) {
        return showtimeRepository.findAllByDate(date);
    }
    public List<Showtime> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieMovieId(movieId);
    }

    public Showtime updateSeatAvailability(Long showtimeId, List<String> selectedSeats) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // Update the seat availability based on selected seats
        Boolean[][] seatGrid = showtime.getSeatGrid();
        for (String seat : selectedSeats) {
            String[] parts = seat.split("-");
            int rowIndex = Integer.parseInt(parts[0]);
            int seatIndex = Integer.parseInt(parts[1]);
            seatGrid[rowIndex][seatIndex] = false; // Mark the seat as unavailable
        }

        // Save the updated showtime and return it
        return showtimeRepository.save(showtime);
    }

    public Showtime updateSeatAvailabilityForCanceledBooking(Long showtimeId, String bookedSeat) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        Boolean[][] seatGrid = showtime.getSeatGrid();
        String[] parts = bookedSeat.split("-");
        int rowIndex = Integer.parseInt(parts[0]);
        int seatIndex = Integer.parseInt(parts[1]);
        seatGrid[rowIndex][seatIndex] = true; // Mark the seat as available again

        return showtimeRepository.save(showtime);
    }
}
