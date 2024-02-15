package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;

import java.util.Optional;

public interface UserService {
    public User registerUser(User user);
    public User loginUser(String username, String password);
    public Optional<User> getUserById(Long userId);


}
