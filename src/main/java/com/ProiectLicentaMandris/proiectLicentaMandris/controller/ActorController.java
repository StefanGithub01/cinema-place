package com.ProiectLicentaMandris.proiectLicentaMandris.controller;

import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Actor;
import com.ProiectLicentaMandris.proiectLicentaMandris.entity.Movie;
import com.ProiectLicentaMandris.proiectLicentaMandris.service.ActorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/actors")
public class ActorController {
    @Autowired
    private ActorService actorService;

    // Get Actors REST API
    @GetMapping("{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable("id") Long actorId) {
        Actor actor = actorService.getActorById(actorId);
        if(actor != null) {
            return ResponseEntity.ok(actor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get All Actors REST API
    @GetMapping("/all")
    public ResponseEntity<List<Actor>> getAllActors() {
        List<Actor> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);
    }
    @PostMapping("/admin/add")
    public ResponseEntity<Actor> addActor(@RequestBody Actor actor) {
        Actor addedActor = actorService.addActor(actor);
        return new ResponseEntity<>(addedActor, HttpStatus.CREATED);
    }
    @PostMapping("/admin/update")
    public ResponseEntity<Actor> updateActor(@RequestBody Actor actor) {
        Actor addedActor = actorService.updateActor(actor);
        return new ResponseEntity<>(addedActor, HttpStatus.OK);
    }


}
