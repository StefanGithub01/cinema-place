package com.ProiectLicentaMandris.proiectLicentaMandris.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {
    private Long actorId;
    private String firstName;
    private String lastName;
    private int age;

}
