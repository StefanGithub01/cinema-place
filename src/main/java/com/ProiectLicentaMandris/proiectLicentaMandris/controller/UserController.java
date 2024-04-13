package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Booking;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.PasswordResetToken;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Review;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.TokenRepository;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.EmailSenderService;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.UserService;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsersNoPasswords() {
        List<User> users = userService.listUsers();
        if(users != null) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("User recieved at Register is" + user.getUsername() + user.getPassword());

        userService.registerUser(user);
        // Send registration email to the registered user
        String subject = "Welcome to Cinema Place!";
        String body = "Thank you for registering on Cinema Place!\n\nYour login information is: \n" +
                      "   username: " + user.getUsername()+ "\n";
        emailSenderService.sendEmail(user.getEmail(), subject, body);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public  ResponseEntity<User>  loginUser(@RequestBody User user) {
        // Retrieve the user from the database by username
        // TESTING
        System.out.println("User received is" + user);

        User storedUser = userService.getUserByUsername(user.getUsername());

        // Check if the user exists and the provided password matches the hashed password
        if (storedUser != null && passwordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
            return new ResponseEntity<>(storedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/{userId}/votedReviews")
    public ResponseEntity<List<Review>> listVotedReviews(@PathVariable Long userId) {
        List<Review> votedReviews = userService.listVotedReviews(userId);
        return ResponseEntity.ok(votedReviews);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        User user = userService.getUserByEmail(email);

            String resetToken = userService.generateResetToken(user);
            //String resetLink = "https://localhost:8080/resetPassword/" + resetToken;
        String resetLink = "http://localhost:3000/resetPassword/" + resetToken;
        String subject = "Password Reset Request";
            String body = "User found with this email: " + user.getUsername() + "\nPlease click the following link to reset your password: " + resetLink;
            emailSenderService.sendEmail(email, subject, body);
            return new ResponseEntity<>("Reset email sent successfully", HttpStatus.OK);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        if (userService.isValidResetToken(token)) {
            userService.resetPassword(token, password);
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
    //@GetMapping("/api/users/validateToken")


}
