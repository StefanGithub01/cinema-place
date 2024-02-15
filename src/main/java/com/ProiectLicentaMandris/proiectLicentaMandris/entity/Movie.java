package com.ProiectLicentaMandris.proiectLicentaMandris.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String genre;
    private String backdrop;
    private String posterImageUrl;
    private String trailerLink;

    @OneToMany(mappedBy = "movie")
    @JsonIgnoreProperties("movie") // Add this to prevent recursion
    private List<Review> reviews;
    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "actorId")
    )
    private List<Actor> actors;

}
