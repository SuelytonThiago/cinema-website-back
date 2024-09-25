package com.example.project.rest.controllers;

import com.example.project.domain.entities.Sessions;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.dto.SessionRequestDto;
import com.example.project.rest.dto.SessionResponseDto;
import com.example.project.rest.services.SessionsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionsService sessionsService;


    @PostMapping("/add")
    public ResponseEntity<Void> addSession(@RequestBody @Valid SessionRequestDto dto){
        sessionsService.createSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<SessionResponseDto> getInfoSession(@PathVariable Long id){
        return ResponseEntity.ok(sessionsService.getSessionInformation(id));
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<SessionResponseDto>> findByMovie(@PathVariable Long id) {
        return ResponseEntity.ok(sessionsService.findSessionsByMovie(id));
    }

    @GetMapping
    public ResponseEntity<List<SessionResponseDto>>getAll(){
        return ResponseEntity.ok(sessionsService.getAllSessions());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateData(@PathVariable @Valid SessionRequestDto dto,
                                           @PathVariable Long id){
        sessionsService.updateSession(id,dto);
        return ResponseEntity.noContent().build();
    }

}
