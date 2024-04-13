package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.dto.MovieAssemblerDto;
import com.ProiectLicentaMandris.proiectLicentaMandris.dto.ReviewAssemblerDto;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.ReviewRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    private final MovieService movieService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, UserService userService, MovieService movieService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.movieService = movieService;
    }

    public List<Review> getReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieMovieId(movieId);
    }

    public List<Review> allReviews() {
        return reviewRepository.findAll();
    }




    public Review updateReview(ReviewAssemblerDto reviewFormData) {
        // Find the existing review by reviewId
        Optional<Review> optionalReview = reviewRepository.findById(reviewFormData.getReviewId());
        if (optionalReview.isPresent()) {
            // Get the existing review entity
            Review existingReview = optionalReview.get();

            // Update the existing review entity with the new data
            BeanUtils.copyProperties(reviewFormData, existingReview, "reviewId", "date");
            existingReview.setDate(LocalDateTime.now()); // Update the date

            // Save the updated review entity
            return reviewRepository.save(existingReview);
        } else {
            // Handle if the review is not found
            throw new RuntimeException("Review not found with ID: " + reviewFormData.getReviewId());
        }
    }

    public Review createReview(Long userId, Long movieId, int ratingScore, String comment, String title) {
        // Retrieve User and Movie entities using service methods
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Movie movie = movieService.singleMovie(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));

        int agreeCount = 0;
        int disagreeCount = 0;
        // Create and save the review
        Review review = new Review(user, movie, ratingScore, comment, title, agreeCount, disagreeCount);
        return reviewRepository.save(review);
    }
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }


    private void removeVotesForReview(Review review) {
        List<User> users = userRepository.findUsersByVotedReviewsContains(review);
        for (User user : users) {
            user.getVotedReviews().remove(review);
            userRepository.save(user); // Update the user to reflect the change
        }
    }

    public void deleteReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            // Remove the review from the users who have voted for it
            removeVotesForReview(review);
            // Delete the review
            reviewRepository.deleteById(reviewId);
        }
    }



    public void agreeWithReview(Long userId, Long reviewId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

            // Update the user's votedReviews set
            user.getVotedReviews().add(review);
            userRepository.save(user);

            // Update the review's agreeCount
            review.setAgreeCount(review.getAgreeCount() + 1);
            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

    public void disagreeWithReview(Long userId, Long reviewId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

            // Update the user's votedReviews set
            user.getVotedReviews().add(review);
            userRepository.save(user);

            // Update the review's disagreeCount
            review.setDisagreeCount(review.getDisagreeCount() + 1);
            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

}

