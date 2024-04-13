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

    @Column(columnDefinition = "LONGTEXT")
    private String title; // New field for review title

    private int ratingScore;
    @Column(columnDefinition = "LONGTEXT")
    private String comment;
    private LocalDateTime date;
    private int agreeCount;
    private int disagreeCount;

    public Review(User user, Movie movie, int ratingScore, String comment, String title, int agreeCount, int disagreeCount) {
        this.user = user;
        this.movie = movie;
        this.ratingScore = ratingScore;
        this.comment = comment;
        this.title = title;
        this.date = LocalDateTime.now(); // Initialize date with the current timestamp
        this.agreeCount = agreeCount;
        this.disagreeCount = disagreeCount;
    }

}
