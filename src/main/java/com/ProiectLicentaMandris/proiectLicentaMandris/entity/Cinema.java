package com.ProiectLicentaMandris.proiectLicentaMandris.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cinemaId;
    private String name;
    private String location;
    private int capacity;
    @ManyToMany(mappedBy = "cinemas")
    @JsonIgnore
    //@JsonIgnoreProperties("cinemas")
    private List<Movie> movies;

}
