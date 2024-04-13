package com.ProiectLicentaMandris.proiectLicentaMandris.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    private String title;
    private String releaseDate;
    private String director;
    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    private String backdrop;
    private String posterImageUrl;
    private String trailerLink;
    @Enumerated(value = EnumType.STRING)
    private Rating rated;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private Duration duration;
    private String imdbMovieId;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    //@JsonIgnoreProperties("movie") // Add this to prevent recursion
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "actorId")
    )
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "cinema_movies",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "cinema_id")
    )
    private List<Cinema> cinemas;



}
