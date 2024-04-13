package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Cinema;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {
    @Autowired
    CinemaRepository cinemaRepository;

    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    public Optional<Cinema> getCinemaById(Long cinemaId) {
        return cinemaRepository.findById(cinemaId);
    }

    public Cinema addCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    public Cinema updateCinema(Cinema updatedCinema) {
        Cinema cinema = cinemaRepository.findById(updatedCinema.getCinemaId())
                .orElseThrow(() -> new IllegalArgumentException("Cinema not found with id " + updatedCinema.getCinemaId()));
        cinema.setName(updatedCinema.getName());
        cinema.setLocation(updatedCinema.getLocation());
        cinema.setCapacity(updatedCinema.getCapacity());

        return cinemaRepository.save(cinema);
    }
}
