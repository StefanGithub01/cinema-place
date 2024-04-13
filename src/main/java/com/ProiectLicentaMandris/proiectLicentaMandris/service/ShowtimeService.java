package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Cinema;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {
    @Autowired
    ShowtimeRepository showtimeRepository;
    @Autowired
    private  MovieService movieService;
    @Autowired
    private CinemaService cinemaService;

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

    // Email Logic
    public Optional<Movie> getMovieByShowtimeId(Long showtimeId) {
        Optional<Showtime> showtimeOptional = showtimeRepository.findById(showtimeId);
        return showtimeOptional.map(Showtime::getMovie);
    }

    public Optional<Showtime> getShowtimeById(Long showtimeId) {
        Optional<Showtime> showtimeOptional = showtimeRepository.findById(showtimeId);
        if (showtimeOptional.isPresent()) {
            Showtime showtime = showtimeOptional.get();
            return Optional.of(showtime);
        } else {
            return Optional.empty();
        }
    }

    public List<Movie> findMoviesWithShowtimesOnDate(LocalDate date) {
        // Get showtimes for the given date
        List<Showtime> showtimes = showtimeRepository.findByStartTimeBetween(
                date.atStartOfDay(),
                date.atStartOfDay().plusDays(1)
        );

        // Extract movies from the showtimes
        return showtimes.stream()
                .map(Showtime::getMovie)
                .distinct() // Remove duplicates if a movie has multiple showtimes on the same day
                .collect(Collectors.toList());
    }

    public List<Showtime> getShowtimesForCinema(Long movieId, Long cinemaId) {
        return showtimeRepository.findByMovieMovieIdAndCinemaCinemaId(movieId, cinemaId);
    }
    public List<Movie> findMoviesWithShowtimesOnDateatCinema(LocalDate date, Long cinemaId) {
        // Get showtimes for the given date
        List<Showtime> showtimes = showtimeRepository.findByStartTimeBetweenAndCinemaCinemaId(
                date.atStartOfDay(),
                date.atStartOfDay().plusDays(1),
                cinemaId
        );

        // Extract movies from the showtimes
        return showtimes.stream()
                .map(Showtime::getMovie)
                .distinct() // Remove duplicates if a movie has multiple showtimes on the same day
                .collect(Collectors.toList());
    }


    public Showtime addShowtime(Long movieId, Long cinemaId, LocalDateTime startTime, LocalDateTime endTime, List<List<Boolean>> seatGrid) {
        Movie movie = movieService.singleMovie(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));

        Cinema cinema = cinemaService.getCinemaById(cinemaId)
                .orElseThrow(() -> new IllegalArgumentException("Cinema not found with id: " + cinemaId));

        // Convert seat grid from List<List<Boolean>> to Boolean[][]
        Boolean[][] seatGridArray = new Boolean[seatGrid.size()][];
        for (int i = 0; i < seatGrid.size(); i++) {
            List<Boolean> row = seatGrid.get(i);
            seatGridArray[i] = row.toArray(new Boolean[row.size()]);
        }

        Showtime showtime = new Showtime(movie, cinema, startTime, endTime, seatGridArray);
        return showtimeRepository.save(showtime);
    }


    public Showtime editShowtime(Long showtimeId, Long movieId, Long cinemaId, LocalDateTime startTime, LocalDateTime endTime, List<List<Boolean>> seatGrid) {
        // Find the showtime by its ID
        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);

        // Check if the showtime exists
        if (!optionalShowtime.isPresent()) {
            throw new RuntimeException("Showtime not found");
        }

        // Retrieve the movie and cinema
        Movie movie = movieService.singleMovie(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));
        Cinema cinema = cinemaService.getCinemaById(cinemaId)
                .orElseThrow(() -> new IllegalArgumentException("Cinema not found with id: " + cinemaId));

        // Extract the actual showtime object from the optional
        Showtime showtime = optionalShowtime.get();

        // Update the showtime with the new data
        showtime.setMovie(movie);
        showtime.setCinema(cinema);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
        // Convert seat grid from List<List<Boolean>> to Boolean[][]
        Boolean[][] seatGridArray = new Boolean[seatGrid.size()][];
        for (int i = 0; i < seatGrid.size(); i++) {
            List<Boolean> row = seatGrid.get(i);
            seatGridArray[i] = row.toArray(new Boolean[row.size()]);
        }
        showtime.setSeatGrid(seatGridArray);

        // Save the updated showtime
        return showtimeRepository.save(showtime);
    }



    /*
    not working - delete

    public Showtime addShowtime(Showtime showtime) {
        // Add any validation logic here before saving the movie
        return showtimeRepository.save(showtime);
    }
         */


}
