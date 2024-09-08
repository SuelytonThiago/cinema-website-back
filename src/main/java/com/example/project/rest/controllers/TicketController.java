package com.example.project.rest.controllers;

import com.example.project.rest.dto.TicketRequestDto;
import com.example.project.rest.dto.TicketsResponseDto;
import com.example.project.rest.services.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/add")
    public ResponseEntity<Void> addTicket(@RequestBody @Valid TicketRequestDto dto, HttpServletRequest request){
        ticketService.createTicket(dto,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<TicketsResponseDto>> getTickets(HttpServletRequest request){
        return ResponseEntity.ok(ticketService.getTickets(request));
    }

    @PatchMapping("/invalidate/{id}")
    public ResponseEntity<Void> invalidateTicket(@PathVariable Long id){
        ticketService.invalidateTicket(id);
        return ResponseEntity.noContent().build();
    }
}
