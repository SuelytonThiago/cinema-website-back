package com.example.project.rest.controllers;

import com.example.project.rest.dto.TicketRequestDto;
import com.example.project.rest.dto.TicketsResponseDto;
import com.example.project.rest.services.TicketService;
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

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchaseTicket(@RequestBody @Valid TicketRequestDto dto){
        ticketService.createTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TicketsResponseDto>> getTickets(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.getTickets(id));
    }

    @PatchMapping("/invalidateQ{id}")
    public ResponseEntity<Void> invalidateTicket(@PathVariable Long id){
        ticketService.invalidateTicket(id);
        return ResponseEntity.noContent().build();
    }
}
