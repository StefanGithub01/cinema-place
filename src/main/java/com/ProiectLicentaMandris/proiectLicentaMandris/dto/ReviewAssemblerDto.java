package com.ProiectLicentaMandris.proiectLicentaMandris.dto;

import lombok.Data;

@Data
public class ReviewAssemblerDto {
    private Long reviewId;
    private Long userId;
    private Long movieId;
    private int ratingScore;
    private String comment;
    private String title;

}
