package com.ProiectLicentaMandris.proiectLicentaMandris.repository;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Booking;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserUserId(Long userId);

}
