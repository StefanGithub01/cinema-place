package com.ProiectLicentaMandris.proiectLicentaMandris.entity;


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
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "showtimeId")
    private Showtime showtime;
    @Column(columnDefinition = "TEXT") // Use appropriate column type for your database
    private String bookedSeats;
    private LocalDateTime date;

}


