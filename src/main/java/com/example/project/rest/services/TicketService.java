package com.example.project.rest.services;

import com.example.project.domain.entities.Chairs;
import com.example.project.domain.entities.Tickets;
import com.example.project.domain.repositories.TicketsRepository;
import com.example.project.rest.dto.TicketRequestDto;
import com.example.project.rest.dto.TicketsResponseDto;
import com.example.project.rest.services.exceptions.CustomException;
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

        if(!session.isChairAvailable(dto.getChairNumber())){
            throw new CustomException("the chair is already occupied");
        }

        if(dto.getChairNumber() < 0 || dto.getChairNumber() >= session.getChairsAvailable().length){
            throw new CustomException("the chair number is invalid");
        }

        session.reserveChair(dto.getChairNumber());

        var chair = new Chairs();
        chair.setUser(user);
        chair.setSession(session);
        chair.setNumber(dto.getChairNumber());
        var savedChair = chairService.saveChair(chair);
        ticketsRepository.save(Tickets.of(dto, user, session, savedChair));
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
