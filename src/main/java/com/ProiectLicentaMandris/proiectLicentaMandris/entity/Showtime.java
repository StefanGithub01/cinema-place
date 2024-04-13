package com.ProiectLicentaMandris.proiectLicentaMandris.entity;

import com.ProiectLicentaMandris.proiectLicentaMandris.SeatGridConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showtimeId;
    @ManyToOne
    @JoinColumn(name = "movieId")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "cinemaId")
    private Cinema cinema;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(columnDefinition = "JSON", name = "seat_grid")
    @Convert(converter = SeatGridConverter.class)
    private Boolean[][] seatGrid;

    public Showtime(Movie movie, Cinema cinema, LocalDateTime startTime, LocalDateTime endTime, Boolean[][] seatGrid) {
        this.movie = movie;
        this.cinema = cinema;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seatGrid = seatGrid;
    }
}
