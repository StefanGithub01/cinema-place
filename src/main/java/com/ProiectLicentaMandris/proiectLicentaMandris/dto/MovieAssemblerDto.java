package com.ProiectLicentaMandris.proiectLicentaMandris.dto;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import lombok.Data;

import java.util.List;

@Data
public class MovieAssemblerDto {
    private Movie movie;
    private List<Long> selectedActorIds;
    private List<Long> selectedCinemaIds;
}
