package com.ProiectLicentaMandris.proiectLicentaMandris.repository;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.PasswordResetToken;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}
