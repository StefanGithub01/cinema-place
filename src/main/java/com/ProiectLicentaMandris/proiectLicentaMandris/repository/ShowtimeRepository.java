package com.ProiectLicentaMandris.proiectLicentaMandris.repository;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s WHERE DATE(s.startTime) = :date")
    List<Showtime> findAllByDate(@Param("date") LocalDate date);

    List<Showtime> findByMovieMovieId(Long movieId);

    List<Showtime> findByStartTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Showtime> findByMovieMovieIdAndCinemaCinemaId(Long movieId, Long cinemaId);
    List<Showtime> findByStartTimeBetweenAndCinemaCinemaId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long cinemaId);
    void deleteByMovie(Movie movie);



}

