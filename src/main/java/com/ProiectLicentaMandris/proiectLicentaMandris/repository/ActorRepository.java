package com.ProiectLicentaMandris.proiectLicentaMandris.repository;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {

}
