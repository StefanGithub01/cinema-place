package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Cinema;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.CinemaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/cinema")
public class CinemaController {
    @Autowired
    CinemaService cinemaService;

    @GetMapping("/all")
    ResponseEntity<List<Cinema>> getAllCinemas() {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        return ResponseEntity.ok(cinemas);
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Cinema> addCinema(@RequestBody Cinema cinema) {
        Cinema addedCinema = cinemaService.addCinema(cinema);
        return new ResponseEntity<>(addedCinema, HttpStatus.CREATED);
    }
    @PostMapping("/admin/update")
    public ResponseEntity<Cinema> updateCinema(@RequestBody Cinema cinema) {
        Cinema updatedCinema = cinemaService.updateCinema(cinema);
        return new ResponseEntity<>(updatedCinema, HttpStatus.OK);
    }



}
