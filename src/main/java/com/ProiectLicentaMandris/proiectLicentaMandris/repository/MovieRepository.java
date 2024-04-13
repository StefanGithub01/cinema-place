package com.ProiectLicentaMandris.proiectLicentaMandris.repository;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Genre;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(Genre genre);

    List<Movie> findByDirector(String director);

    List<Movie> findByActorsActorId(Long actorId);

    List<Movie> findByReleaseDateStartingWith(String year);

    List<Movie> findByGenreAndReleaseDateStartingWith(Genre genre, int releaseYear);

    List<Movie> findByTitle(String title);

    List<Movie> findByTitleContainingIgnoreCase(String partialTitle);

    List<Movie> findByCinemasCinemaId(Long cinemaId);

    // experiment
    List<Movie> findByTitleContainingIgnoreCaseAndCinemasCinemaId(String title, Long cinemaId);

    List<Movie> findByGenreAndCinemasCinemaId(Genre genre, Long cinemaId);
    List<Movie> findByDurationBetweenAndCinemasCinemaId(Duration minDuration, Duration maxDuration, Long cinemaId);

    @Query("SELECT DISTINCT s.movie FROM Showtime s " +
            "JOIN s.cinema c " +
            "WHERE (:cinemaId IS NULL OR c.cinemaId = :cinemaId) " +
            "AND (:releaseYear IS NULL OR SUBSTRING(s.movie.releaseDate, 1, 4) = :releaseYear) " +
            "AND (:minDuration IS NULL OR s.movie.duration >= :minDuration) " +
            "AND (:maxDuration IS NULL OR s.movie.duration <= :maxDuration) " +
            "AND (:genre IS NULL OR s.movie.genre = :genre) " +
            "AND (:title IS NULL OR LOWER(s.movie.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:startOfDay IS NULL OR s.startTime >= :startOfDay AND s.startTime <= :endOfDay)")
    List<Movie> findByFilters(@Param("cinemaId") Long cinemaId,
                              @Param("releaseYear") String releaseYear,
                              @Param("minDuration") Duration minDuration,
                              @Param("maxDuration") Duration maxDuration,
                              @Param("genre") Genre genre,
                              @Param("title") String title,
                              @Param("startOfDay") LocalDateTime startOfDay,
                              @Param("endOfDay") LocalDateTime endOfDay);

}
