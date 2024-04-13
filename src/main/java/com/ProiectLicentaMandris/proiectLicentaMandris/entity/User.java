package com.ProiectLicentaMandris.proiectLicentaMandris.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private Boolean isAdmin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_review_votes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    @JsonIgnoreProperties("user") // Add this to prevent recursion
    private Set<Review> votedReviews = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIgnore
    private PasswordResetToken passwordResetToken;

}
