package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.dto.MovieAssemblerDto;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.*;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }
    public Optional<Movie> singleMovie(Long movieId) {
        return movieRepository.findById(movieId);
    }
    public List<Movie> getMoviesByGenre(Genre genre) {
        return movieRepository.findByGenre(genre);
    }

    public List<Movie> getMoviesByDirector(String director) {
        return movieRepository.findByDirector(director);
    }
    public List<Actor> getActorsForMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));
        return movie.getActors();

    }
    public List<Movie> getMoviesForActor(Long actorId) {
        return movieRepository.findByActorsActorId(actorId);
    }

    public List<Movie> getMoviesByYear(int year) {
        return movieRepository.findByReleaseDateStartingWith(Integer.toString(year));
    }

    public List<Movie> getMoviesByGenreAndYear(Genre genre, int year) {
        return movieRepository.findByGenreAndReleaseDateStartingWith(genre, year);
    }

    public List<Movie> getMoviesByPartialTitle(String partialTitle) {
        return movieRepository.findByTitleContainingIgnoreCase(partialTitle);
    }

    public List<Movie> getMoviesByPartialTitleAndCinemaId(String partialTitle, Long cinemaId) {
        return movieRepository.findByTitleContainingIgnoreCaseAndCinemasCinemaId(partialTitle, cinemaId);
    }

    public List<Movie> getMoviesByCinemaId(Long cinemaId) {
        return movieRepository.findByCinemasCinemaId(cinemaId);
    }

    public List<Movie> getMoviesByGenreAndCinemaId(Genre genre, Long cinemaId){
        return movieRepository.findByGenreAndCinemasCinemaId(genre, cinemaId);
    }

    public Genre[] getAllGenres() {
        return Genre.values();
    }
    public Rating[] getAllRatings() {
        return Rating.values();
    }

    public List<Movie> getMoviesByDurationRangeAndCinemaId(Duration minDuration, Duration maxDuration, Long cinemaId) {
        // Perform the query to find movies within the duration range
        return movieRepository.findByDurationBetweenAndCinemasCinemaId(minDuration, maxDuration, cinemaId);
    }
    public Movie addMovie(MovieAssemblerDto movieFormData) {
        Movie movie = movieFormData.getMovie();
        List<Long> selectedActorIds = movieFormData.getSelectedActorIds();
        List<Long> selectedCinemaIds = movieFormData.getSelectedCinemaIds();

        // Fetch actors and cinemas based on the selected IDs
        List<Actor> selectedActors = actorRepository.findAllById(selectedActorIds);
        List<Cinema> selectedCinemas = cinemaRepository.findAllById(selectedCinemaIds);

        // Associate the fetched actors and cinemas with the movie
        movie.setActors(selectedActors);
        movie.setCinemas(selectedCinemas);

        // Save the movie
        return movieRepository.save(movie);
    }

    public Movie updateMovie(MovieAssemblerDto movieFormData) {

        Long movieId = movieFormData.getMovie().getMovieId();
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id " + movieId));

        // Update other movie properties
        movie.setTitle(movieFormData.getMovie().getTitle());
        movie.setBackdrop(movieFormData.getMovie().getBackdrop());
        movie.setDirector(movieFormData.getMovie().getDirector());
        movie.setGenre(movieFormData.getMovie().getGenre());
        movie.setRated(movieFormData.getMovie().getRated()); // Update age rating here
        movie.setDescription(movieFormData.getMovie().getDescription());
        movie.setDuration(movieFormData.getMovie().getDuration());
        movie.setImdbMovieId(movieFormData.getMovie().getImdbMovieId());
        movie.setPosterImageUrl(movieFormData.getMovie().getPosterImageUrl());
        movie.setReleaseDate(movieFormData.getMovie().getReleaseDate());
        movie.setTrailerLink(movieFormData.getMovie().getTrailerLink());

        // Update actors and cinemas
        List<Long> selectedActorIds = movieFormData.getSelectedActorIds();
        List<Long> selectedCinemaIds = movieFormData.getSelectedCinemaIds();
        List<Actor> selectedActors = actorRepository.findAllById(selectedActorIds);
        List<Cinema> selectedCinemas = cinemaRepository.findAllById(selectedCinemaIds);
        movie.setActors(selectedActors);
        movie.setCinemas(selectedCinemas);

        return movieRepository.save(movie);
    }
    @Transactional
    public void deleteMovie(Long movieId) {
        try {
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new EntityNotFoundException("Movie not found with ID: " + movieId));

            List<Booking> bookings = bookingRepository.findByShowtime_Movie(movie);

            for (Booking booking : bookings) {
                User user = booking.getUser();
                sendBookingCancellationEmail(user, movie.getTitle());
            }
            for (Booking booking : bookings) {
                bookingRepository.delete(booking);
            }

            showtimeRepository.deleteByMovie(movie);
            reviewRepository.deleteByMovie(movie);

            // Remove associations with actors
            movie.getActors().clear();

            // Remove associations with cinemas
            movie.getCinemas().clear();

            // Delete the movie
            movieRepository.delete(movie);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error deleting movie with ID: {}", movieId, e);
            throw e; // rethrow the exception to be handled by the controller
        }
    }

    private void sendBookingCancellationEmail(User user, String movieTitle) {
        String subject = "Booking Cancellation: " + movieTitle;
        String body = "Dear " + user.getUsername() + ",\n\n" +
                "We regret to inform you that all of your bookings for the movie '" + movieTitle + "' have been canceled " +
                "due to unforeseen circumstances. We apologize for any inconvenience caused.\n\n" +
                "Thank you for your understanding.\n\n" +
                "Sincerely,\n" +
                "Cinema Place Team";
        emailSenderService.sendEmail(user.getEmail(), subject, body);
    }

    public List<Movie> getFilteredMovies(Long cinemaId, String releaseYear, Integer duration, Genre genre, String title, LocalDate showtimeDate) {
        // You can calculate minDuration and maxDuration based on the provided duration
        Duration minDuration = (duration != null) ? Duration.ofMinutes(duration - 15) : null;
        Duration maxDuration = (duration != null) ? Duration.ofMinutes(duration + 15) : null;

        // Convert showtimeDate to LocalDateTime range
        LocalDateTime startOfDay = (showtimeDate != null) ? showtimeDate.atStartOfDay() : null;
        LocalDateTime endOfDay = (showtimeDate != null) ? showtimeDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59) : null;

        // Call the repository method with the provided filters
        return movieRepository.findByFilters(cinemaId, releaseYear, minDuration, maxDuration, genre, title, startOfDay, endOfDay);
    }

 }
