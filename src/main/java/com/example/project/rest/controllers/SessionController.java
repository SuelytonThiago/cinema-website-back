package com.example.project.rest.controllers;

import com.example.project.domain.entities.Sessions;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.dto.SessionRequestDto;
import com.example.project.rest.dto.SessionResponseDto;
import com.example.project.rest.services.SessionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@SecurityRequirement(name = "bearerAuth")
public class SessionController {

    @Autowired
    private SessionsService sessionsService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/add")
    @Operation(summary = "add a new session")
    public ResponseEntity<Void> addSession(@RequestBody @Valid SessionRequestDto dto, HttpServletRequest request){
        var response = sessionsService.createSession(dto, request);

        messagingTemplate.convertAndSend("/topic/sessions", response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/info/{id}")
    @Operation(summary = "get session information")
    public ResponseEntity<SessionResponseDto> getInfoSession(@PathVariable Long id){
        return ResponseEntity.ok(sessionsService.getSessionInformation(id));
    }

    @GetMapping("/movie/{id}")
    @Operation(summary = "search sessions by film")
    public ResponseEntity<List<SessionResponseDto>> findByMovie(@PathVariable Long id) {
        return ResponseEntity.ok(sessionsService.findSessionsByMovie(id));
    }

    @GetMapping
    @Operation(summary = "search all sessions")
    public ResponseEntity<List<SessionResponseDto>>getAll(){
        return ResponseEntity.ok(sessionsService.getAllSessions());
    }


}
