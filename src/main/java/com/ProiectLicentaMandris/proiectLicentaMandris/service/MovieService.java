package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }
    public Optional<Movie> singleMovie(Long movieId) {
        return movieRepository.findById(movieId);
    }
    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public List<Actor> getActorsForMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));
        return movie.getActors();

    }
    public List<Movie> getMoviesForActor(Long actorId) {
        return movieRepository.findByActorsActorId(actorId);
    }

}
