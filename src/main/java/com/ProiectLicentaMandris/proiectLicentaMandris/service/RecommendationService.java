package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MovieService movieService;

    public List<Movie> generateMovieRecommendations(Long userId) {
        // Retrieve user's bookings and reviews
        List<Booking> bookings = bookingService.getBookingByUserId(userId);
        List<Review> reviews = reviewService.getReviewsByUserId(userId);

        // Analyze user's history and preferences
        List<Review> highRatedReviews = reviews.stream()
                .filter(review -> review.getRatingScore() > 6)
                .collect(Collectors.toList());

        Map<Genre, Long> genreOccurrences = countOccurrencesOfGenres(highRatedReviews);
        Map<Actor, Long> actorOccurrences = countOccurrencesOfActors(highRatedReviews);
        Map<String, Long> directorOccurrences = countOccurrencesOfDirectors(highRatedReviews);

        // Filter out favorites with less than 2 occurrences
        List<Genre> favoriteGenres = filterFavorites(genreOccurrences, 2);
        List<Actor> favoriteActors = filterFavorites(actorOccurrences, 2);
        List<String> favoriteDirectors = filterFavorites(directorOccurrences, 2);

        // Generate recommendations based on user's preferences
        Set<Movie> recommendedMovies = new HashSet<>();
        for (Genre genre : favoriteGenres) {
            recommendedMovies.addAll(movieService.getMoviesByGenre(genre));
        }
        for (Actor actor : favoriteActors) {
            recommendedMovies.addAll(movieService.getMoviesForActor(actor.getActorId()));
        }
        for (String director : favoriteDirectors) {
            recommendedMovies.addAll(movieService.getMoviesByDirector(director));
        }

        // Remove movies the user has already booked or reviewed
        recommendedMovies.removeAll(bookings.stream().map(Booking::getShowtime).map(Showtime::getMovie).collect(Collectors.toSet()));
        recommendedMovies.removeAll(reviews.stream().map(Review::getMovie).collect(Collectors.toSet()));

        return new ArrayList<>(recommendedMovies);
    }

    private Map<Genre, Long> countOccurrencesOfGenres(List<Review> reviews) {
        return countOccurrences(reviews.stream()
                .map(review -> review.getMovie().getGenre())
                .collect(Collectors.toList()));
    }

    private Map<Actor, Long> countOccurrencesOfActors(List<Review> reviews) {
        return countOccurrences(reviews.stream()
                .flatMap(review -> review.getMovie().getActors().stream())
                .collect(Collectors.toList()));
    }

    private Map<String, Long> countOccurrencesOfDirectors(List<Review> reviews) {
        return countOccurrences(reviews.stream()
                .map(review -> review.getMovie().getDirector())
                .collect(Collectors.toList()));
    }

    private <T> Map<T, Long> countOccurrences(List<T> items) {
        return items.stream()
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
    }

    private <T> List<T> filterFavorites(Map<T, Long> occurrences, long threshold) {
        return occurrences.entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean hasUserReviewedMovie(List<Review> reviews, Movie movie) {
        return reviews.stream().anyMatch(review -> review.getMovie().equals(movie));
    }

    private boolean hasUserBookedMovie(List<Booking> bookings, Movie movie) {
        return bookings.stream().anyMatch(booking -> booking.getShowtime().getMovie().equals(movie));
    }

}
