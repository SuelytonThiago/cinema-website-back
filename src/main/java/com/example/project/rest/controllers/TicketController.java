package com.example.project.rest.controllers;

import com.example.project.rest.dto.TicketRequestDto;
import com.example.project.rest.dto.TicketsResponseDto;
import com.example.project.rest.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/add")
    @Operation(summary = "buy a new ticket")
    public ResponseEntity<Void> addTicket(@RequestBody @Valid TicketRequestDto dto, HttpServletRequest request){
        ticketService.createTicket(dto,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "get user tickets")
    public ResponseEntity<List<TicketsResponseDto>> getTickets(HttpServletRequest request){
        return ResponseEntity.ok(ticketService.getTickets(request));
    }

    @PatchMapping("/invalidate/{id}")
    @Operation(summary = "invalidate ticket")
    public ResponseEntity<Void> invalidateTicket(@PathVariable Long id){
        ticketService.invalidateTicket(id);
        return ResponseEntity.noContent().build();
    }
}
