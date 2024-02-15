package com.ProiectLicentaMandris.proiectLicentaMandris.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties("reviews") // Add this to prevent recursion
    private User user;

    @ManyToOne
    @JoinColumn(name = "movieId")
    @JsonIgnoreProperties("reviews") // Add this to prevent recursion
    private Movie movie;

    private int ratingScore;
    private String comment;
    private LocalDateTime date;

    // Add a constructor with parameters
    public Review(User user, Movie movie, int ratingScore, String comment) {
        this.user = user;
        this.movie = movie;
        this.ratingScore = ratingScore;
        this.comment = comment;
        this.date = LocalDateTime.now(); // Initialize date with the current timestamp
    }
}
