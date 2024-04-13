package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Cinema;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.ShowtimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {
    @Autowired
    ShowtimeService showTimeService;

    @GetMapping("/all")
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        List<Showtime> showTimes = showTimeService.getAllShowTimes();
        return ResponseEntity.ok(showTimes);
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<Showtime>> getAllShowtimes(@PathVariable LocalDate date) {
        List<Showtime> showTimes = showTimeService.getShowTimesByDate(date);
        return ResponseEntity.ok(showTimes);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Showtime>> getShowtimesByMovieId(@PathVariable Long movieId) {
        List<Showtime> showtimes = showTimeService.getShowtimesByMovieId(movieId);
        return ResponseEntity.ok(showtimes);
    }


    // experiment Cinema selected select
    @GetMapping("/movies/cinema")
    public ResponseEntity<List<Movie>> getMoviesWithShowtimesOnDateatCinema(@RequestParam("date") LocalDate date, @RequestParam(required = false) Long cinemaId) {
        try {
            List<Movie> movies;
            if (cinemaId == null) {
                // Call the service method with the LocalDate parameter
                movies = showTimeService.findMoviesWithShowtimesOnDate(date);
            } else {
                movies = showTimeService.findMoviesWithShowtimesOnDateatCinema(date, cinemaId);
            }
            return ResponseEntity.ok(movies);

        } catch (DateTimeParseException e) {
            // Handle invalid date format
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMoviesWithShowtimesOnDate(@RequestParam("date") LocalDate date) {
        try {
            // Call the service method with the LocalDate parameter
            List<Movie> movies = showTimeService.findMoviesWithShowtimesOnDate(date);
            return ResponseEntity.ok(movies);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/movie/{movieId}/cinema")
    public List<Showtime> getShowtimesForCinema(@PathVariable Long movieId, @RequestParam(required = false) Long cinemaId) {
        if(cinemaId == null) {
            return showTimeService.getShowtimesByMovieId(movieId);
        } else {
            return showTimeService.getShowtimesForCinema(movieId, cinemaId);
        }
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Showtime> addShowtime(@RequestBody Map<String, Object> showtimeData) {
        Long movieId = Long.valueOf(showtimeData.get("movieId").toString());
        Long cinemaId = Long.valueOf(showtimeData.get("cinemaId").toString());
        LocalDateTime startTime = LocalDateTime.parse((String) showtimeData.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse((String) showtimeData.get("endTime"));
        List<List<Boolean>> seatGrid = (List<List<Boolean>>) showtimeData.get("seatGrid");

        Showtime addedShowtime = showTimeService.addShowtime(movieId, cinemaId, startTime, endTime, seatGrid);
        return new ResponseEntity<>(addedShowtime, HttpStatus.CREATED);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<Showtime> editShowtime(@RequestBody Map<String, Object> showtimeData) {
        try {

         //System.out.println("Received showtime data: " + showtimeData);

         Long movieId = Long.valueOf(showtimeData.get("movieId").toString());
        Long cinemaId = Long.valueOf(showtimeData.get("cinemaId").toString());
        LocalDateTime startTime = LocalDateTime.parse((String) showtimeData.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse((String) showtimeData.get("endTime"));
        List<List<Boolean>> seatGrid = (List<List<Boolean>>) showtimeData.get("seatGrid");
        Long showtimeId = Long.valueOf(showtimeData.get("showtimeId").toString());

        Showtime editedShowtime = showTimeService.editShowtime(showtimeId, movieId, cinemaId, startTime, endTime, seatGrid);
        return new ResponseEntity<>(editedShowtime, HttpStatus.CREATED);

        } catch (Exception e) {
            // Handle any exceptions here
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
    not working

    @PostMapping("/admin/add")
    public ResponseEntity<Showtime> addMovie(@RequestBody Showtime showtime) {
        Showtime addedShowtime = showTimeService.addShowtime(showtime);
        return new ResponseEntity<>(addedShowtime, HttpStatus.CREATED);
    }
   */
}
