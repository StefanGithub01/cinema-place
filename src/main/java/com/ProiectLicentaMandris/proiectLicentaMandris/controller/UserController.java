package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.User;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.UserService;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public  ResponseEntity<User>  loginUser(@RequestBody User user) {
        User authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
