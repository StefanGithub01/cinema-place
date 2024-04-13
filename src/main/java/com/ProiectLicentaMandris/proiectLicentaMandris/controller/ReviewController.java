package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.dto.MovieAssemblerDto;
import com.ProiectLicentaMandris.proiectLicentaMandris.dto.ReviewAssemblerDto;
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


    @GetMapping("/reviews/{movieId}")
    public ResponseEntity <List<Review>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        if(reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(reviews);
        }
    }
    @GetMapping("/getReview/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
        Optional<Review> optionalReview = reviewService.getReviewById(reviewId);
        if (optionalReview.isPresent()) {
            return new ResponseEntity<>(optionalReview.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<List<Review>> allReviews() {
        return new ResponseEntity<List<Review>>(reviewService.allReviews(), HttpStatus.OK);
    }

    @PostMapping("/createReview")
    public ResponseEntity<Review> createReview(@RequestBody Map<String, Object> reviewData) {
        Long userId = Long.valueOf(reviewData.get("userId").toString());
        Long movieId = Long.valueOf(reviewData.get("movieId").toString());
        String title = reviewData.get("title") != null ? reviewData.get("title").toString() : ""; // Handle null title
        int ratingScore = (int) reviewData.get("ratingScore");
        String comment = reviewData.get("comment") != null ? reviewData.get("comment").toString() : ""; // Handle null comment
        return new ResponseEntity<>(reviewService.createReview(userId, movieId, ratingScore, comment, title), HttpStatus.CREATED);
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

    // Delete review by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removeReview(@PathVariable("id") Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing review: " + e.getMessage());
        }
    }
    @PutMapping("/{reviewId}/{userId}/agree")
    public ResponseEntity<Void> agreeWithReview(@PathVariable Long userId, @PathVariable Long reviewId) {
        try {
            reviewService.agreeWithReview(userId, reviewId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{reviewId}/{userId}/disagree")
    public ResponseEntity<Void> disagreeWithReview(@PathVariable Long userId, @PathVariable Long reviewId) {
        try {
            reviewService.disagreeWithReview(userId, reviewId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/update")
    public ResponseEntity<Review> updateReview(@RequestBody ReviewAssemblerDto ReviewFormData) {
        Review updatedReview = reviewService.updateReview(ReviewFormData);
        return new ResponseEntity<>(updatedReview, HttpStatus.CREATED);
    }

}

