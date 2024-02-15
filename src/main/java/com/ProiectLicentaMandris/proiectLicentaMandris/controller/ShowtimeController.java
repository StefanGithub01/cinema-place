package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.ShowtimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

}
