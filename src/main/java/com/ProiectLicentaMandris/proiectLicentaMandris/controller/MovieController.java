package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allMovies() {
        return new ResponseEntity<List<Movie>> (movieService.allMovies(), HttpStatus.OK);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<Optional<Movie>> getSingleMovie(@PathVariable Long movieId) {
        return new ResponseEntity<>(movieService.singleMovie(movieId), HttpStatus.OK);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Movie>>getMoviesByGenre(@PathVariable String genre) {
        return new ResponseEntity<>(movieService.getMoviesByGenre(genre), HttpStatus.OK);
    }

    @GetMapping("/{movieId}/actors")
    public List<Actor> getActorsForMovie(@PathVariable Long movieId) {
        return movieService.getActorsForMovie(movieId);
    }

    @GetMapping("/actor/{actorId}")
    public List<Movie> getMoviesForActor(@PathVariable Long actorId) {
        return movieService.getMoviesForActor(actorId);
    }
}
