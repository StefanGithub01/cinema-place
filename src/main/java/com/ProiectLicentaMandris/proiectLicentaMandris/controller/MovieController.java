package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.dto.MovieAssemblerDto;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Genre;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Rating;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    // !experimental
@GetMapping("/genre/{genre}/cinema")
    public List<Movie> getMoviesByGenreAndCinema(@PathVariable Genre genre, @RequestParam(required = false) Long cinemaId) {
        if(cinemaId == null) {
            return movieService.getMoviesByGenre(genre);
        } else {
            return movieService.getMoviesByGenreAndCinemaId(genre, cinemaId);
        }
    }

// old make getMoviesByGenreAndCinema fully functional for both cases and remove this.
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Movie>>getMoviesByGenre(@PathVariable Genre genre) {
        return new ResponseEntity<>(movieService.getMoviesByGenre(genre), HttpStatus.OK);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Movie>> getMoviesByYear(@PathVariable int year) {
        List<Movie> movies = movieService.getMoviesByYear(year);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{movieId}/actors")
    public List<Actor> getActorsForMovie(@PathVariable Long movieId) {
        return movieService.getActorsForMovie(movieId);
    }
// experiment
    @GetMapping("/title/{title}")
    public List<Movie> getMoviesByPartialTitle(@PathVariable String title, @RequestParam(required = false) Long cinemaId) {
        if(cinemaId == null) {
            return movieService.getMoviesByPartialTitle(title);
        } else {
            return movieService.getMoviesByPartialTitleAndCinemaId(title, cinemaId);
        }
    }
    /*
        @GetMapping("/title/{title}")
    public List<Movie> getMoviesByPartialTitle(@PathVariable String title) {
        return movieService.getMoviesByPartialTitle(title);
    }
     */
    @GetMapping("/actor/{actorId}")
    public List<Movie> getMoviesForActor(@PathVariable Long actorId) {
        return movieService.getMoviesForActor(actorId);
    }

    @GetMapping("/multi-filter")
    public ResponseEntity<List<Movie>> getMoviesByMultiFilters(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Integer year
    ) {
        List<Movie> movies = movieService.getMoviesByGenreAndYear(genre, year);
        return ResponseEntity.ok(movies);
    }
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<Movie>> getMoviesByCinema(@PathVariable Long cinemaId) {
        List<Movie> movies = movieService.getMoviesByCinemaId(cinemaId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genres")
    public ResponseEntity<Genre[]> getGenres() {
        Genre[] genres = movieService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/ageRatings")
    public ResponseEntity<Rating[]> ageRatings() {
        Rating[] ratings = movieService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/duration/{duration}/cinema")
    public ResponseEntity<List<Movie>> getMoviesByDurationAndCinemaId(@PathVariable int duration, @RequestParam(required = false) Long cinemaId) {
        try {
            // Define the range by subtracting and adding 15 minutes
            Duration minDuration = Duration.ofMinutes(duration - 15);
            Duration maxDuration = Duration.ofMinutes(duration + 15);

            // Call the service method with the duration range
            List<Movie> movies = movieService.getMoviesByDurationRangeAndCinemaId(minDuration, maxDuration, cinemaId);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // ADMINISTRATOR CONTROLS
    @PostMapping("/admin/add")
    public ResponseEntity<Movie> addMovie(@RequestBody MovieAssemblerDto movieFormData) {
        Movie addedMovie = movieService.addMovie(movieFormData);
        return new ResponseEntity<>(addedMovie, HttpStatus.CREATED);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<Movie> updateMovie(@RequestBody MovieAssemblerDto movieFormData) {
        Movie addedMovie = movieService.updateMovie(movieFormData);
        return new ResponseEntity<>(addedMovie, HttpStatus.CREATED);
    }

    @PostMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") Long movieId) {
        try {
            movieService.deleteMovie(movieId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting movie");
        }
    }


    // ALL FILTERS

    @GetMapping("/all-filters")
    public ResponseEntity<List<Movie>> getFilteredMovies(
            @RequestParam(required = false) Long cinemaId,
            @RequestParam(required = false) String releaseYear,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) LocalDate showtimeDate) {
        try {
            /*
            System.out.println("Parameters recieved are:" +
                    "\ncinemaId   " + cinemaId +
                    "\nreleaseYear    " + releaseYear +
                    "\nduration   " + duration +
                    "\ngenre  " + genre +
                    "\ntitle  " + title +
                    "\nshowtimeDate   " + showtimeDate
            );
             */
            // Call the service method with all filter parameters
            List<Movie> movies = movieService.getFilteredMovies(cinemaId, releaseYear, duration, genre, title, showtimeDate);
            System.out.println("Movies sent is: " + movies);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


}
