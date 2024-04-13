package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User registerUser(User user);

    public User loginUser(String username, String password);

    public Optional<User> getUserById(Long userId);

    List<Review> listVotedReviews(Long userId);

    User getUserByUsername(String username);

    List<User> listUsers();

    String generateResetToken(User user);

    public boolean isValidResetToken(String token);

    public void resetPassword(String token, String newPassword);

    User getUserByEmail(String email);

}





