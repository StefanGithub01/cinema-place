package com.ProiectLicentaMandris.proiectLicentaMandris.service.impl;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.UserRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public User loginUser(String username, String password) {
        //finds user by username and checks the password
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; //invalid credentials
    }

}
