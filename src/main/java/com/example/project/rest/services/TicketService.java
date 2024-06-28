package com.example.project.rest.services;

import com.example.project.domain.entities.Tickets;
import com.example.project.domain.repositories.TicketsRepository;
import com.example.project.rest.dto.TicketRequestDto;
import com.example.project.rest.dto.TicketsResponseDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketsRepository ticketsRepository;
    private final ChairService chairService;
    private final SessionsService sessionsService;
    private final UsersService usersService;
    private final JwtService jwtService;

    public Tickets findById(Long id){
        return ticketsRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundExceptions("this ticket is not found"));
    }

    @Transactional
    public void createTicket(TicketRequestDto dto, HttpServletRequest request){
        var userId = jwtService.getClaimId(request);
        var user = usersService.findById(userId);
        var session = sessionsService.findById(dto.getSessionId());
        ticketsRepository.save(Tickets.of(user,session));
    }

    @Transactional
    public List<TicketsResponseDto> getTickets(HttpServletRequest request){
        var userId = jwtService.getClaimId(request);
        var user = usersService.findById(userId);
        return ticketsRepository.findByUser(user).stream().map(TicketsResponseDto::of).collect(Collectors.toList());
    }

    public void invalidateTicket(Long id){
        var ticket = findById(id);
        ticket.setExpired(true);
        ticketsRepository.save(ticket);
    }




}
