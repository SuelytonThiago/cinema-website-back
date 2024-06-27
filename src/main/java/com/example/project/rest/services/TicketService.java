package com.example.project.rest.services;

import com.example.project.domain.entities.Tickets;
import com.example.project.domain.repositories.TicketsRepository;
import com.example.project.rest.dto.TicketRequestDto;
import com.example.project.rest.dto.TicketsResponseDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
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

    public Tickets findById(Long id){
        return ticketsRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundExceptions("this ticket is not found"));
    }

    public void createTicket(TicketRequestDto dto){
        var user = usersService.findById(dto.getUserId());
        var session = sessionsService.findById(dto.getSessionId());
        ticketsRepository.save(Tickets.of(user,session));
    }

    public List<TicketsResponseDto> getTickets(Long userId){
        var user = usersService.findById(userId);
        return ticketsRepository.findByUser(user).stream().map(TicketsResponseDto::of).collect(Collectors.toList());
    }

    public void invalidateTicket(Long id){
        var ticket = findById(id);
        ticket.setExpired(true);
    }




}
