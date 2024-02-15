package com.ProiectLicentaMandris.proiectLicentaMandris.repository;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(String genre);

    List<Movie> findByActorsActorId(Long actorId);

}
