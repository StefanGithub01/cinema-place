package com.ProiectLicentaMandris.proiectLicentaMandris.service;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    public Actor getActorById(Long actorId) {
        return actorRepository.findById(actorId).orElse(null);
    }
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Actor addActor(Actor actor) {
        // Add any validation logic here before saving the movie
        return actorRepository.save(actor);
    }

    public Actor updateActor(Actor actor) {
        Actor updatedActor = actorRepository.findById(actor.getActorId())
                .orElseThrow(() -> new IllegalArgumentException("Actor not found with id " + actor.getActorId()));

        updatedActor.setFirstName(actor.getFirstName());
        updatedActor.setLastName(actor.getLastName());
        updatedActor.setBirthDate(actor.getBirthDate());
        updatedActor.setAvatarImageUrl(actor.getAvatarImageUrl());

        return actorRepository.save(updatedActor);
    }




}
