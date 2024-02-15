package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final MovieService movieService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService, MovieService movieService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.movieService = movieService;
    }

    public List<Review> allReviews() {
        return reviewRepository.findAll();
    }

    public Review createReview(Long userId, Long movieId, int ratingScore, String comment) {
        // Retrieve User and Movie entities using service methods
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Movie movie = movieService.singleMovie(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));

        // Create and save the review
        Review review = new Review(user, movie, ratingScore, comment);
        return reviewRepository.save(review);
    }
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}

