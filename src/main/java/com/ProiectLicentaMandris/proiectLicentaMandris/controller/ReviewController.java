package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Booking;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.expression.common.ExpressionUtils.toLong;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> allReviews() {
        return new ResponseEntity<List<Review>>(reviewService.allReviews(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Review> createReview(@RequestBody Map<String, Object> reviewData) {
        Long userId = Long.valueOf(reviewData.get("userId").toString());
        Long movieId = Long.valueOf(reviewData.get("movieId").toString());
        int ratingScore = (int) reviewData.get("ratingScore");
        String comment = (String) reviewData.get("comment");

        return new ResponseEntity<>(reviewService.createReview(userId, movieId, ratingScore, comment), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable("id") Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        if(reviews != null) {
            return ResponseEntity.ok(reviews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removeReview(@PathVariable("id") Long reviewId) {
        try {
            Optional<Review> review = reviewService.getReviewById(reviewId);
            if (review == null) {
                return ResponseEntity.notFound().build();
            }
            // Delete the booking from the database
            reviewService.deleteReview(reviewId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing review: " + e.getMessage());
        }
    }
}
