package com.ProiectLicentaMandris.proiectLicentaMandris.service.impl;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.PasswordResetToken;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.TokenRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.UserRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    @Override
    public User registerUser(User user) {
        // Encrypt the password before saving
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

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

    @Override
    public List<Review> listVotedReviews(Long userId) {
        // Retrieve the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Return the list of voted reviews for the user
        return user.getVotedReviews().stream().collect(Collectors.toList());
    }

    @Override
    public List<User> listUsers() {
        // Fetch users from the repository
        List<User> users = userRepository.findAll();

        // Remove passwords from user objects
        for (User user : users) {
            user.setPassword(null);
        }

        return users;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return userRepository.findUserByEmail(email);
        } catch(Exception e) {
            throw new RuntimeException("Error occurred while fetching user by email: " + e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }else {
            throw new RuntimeException("User with username" + username + " not found");
        }

    }
    @Override
    public String generateResetToken(User user) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime newExpiryDateTime = currentDateTime.plusMinutes(10);

        // Create a new token
        UUID uuid = UUID.randomUUID();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(newExpiryDateTime);

        // Save the reset token and return the generated token
        try {
            // If there's an existing token, delete it
            PasswordResetToken existingToken = tokenRepository.findByUser(user);
            if (existingToken != null) {
                tokenRepository.delete(existingToken);
            }
            return tokenRepository.save(resetToken).getToken();
        } catch (Exception e) {
            // Handle the exception gracefully, such as logging it
            e.printStackTrace();
            throw new RuntimeException("Failed to generate reset token");
        }
    }
    @Override
    public boolean isValidResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOptional = Optional.ofNullable(tokenRepository.findByToken(token));
        return resetTokenOptional.isPresent() && resetTokenOptional.get().getExpiryDateTime().isAfter(LocalDateTime.now());
    }
    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        User user = resetToken.getUser();
        // Encrypt the new password before saving
        user.setPassword(passwordEncoder.encode(newPassword));
        // Save the user entity back to the repository with the updated password
        userRepository.save(user);
        // Delete the reset token
        tokenRepository.delete(resetToken);
    }


}
