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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    public Tickets findById(Long id){
        return ticketsRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundExceptions(
                        messageSource.getMessage("ticket.service.error.notFound", null, LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public void createTicket(TicketRequestDto dto, HttpServletRequest request){
        var userId = jwtService.getClaimId(request);
        var user = usersService.findById(userId);
        var session = sessionsService.findById(dto.getSessionId());

        if(!session.isChairAvailable(dto.getChairNumber())){
            throw new CustomException(
                    messageSource.getMessage("ticket.service.error.chairOccupied", null, LocaleContextHolder.getLocale())
            );
        }

        if(dto.getChairNumber() < 0 || dto.getChairNumber() >= session.getChairsAvailable().length){
            throw new CustomException(
                    messageSource.getMessage("ticket.service.error.invalidChairNumber", null, LocaleContextHolder.getLocale())
            );
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

        var list =  ticketsRepository.findByUser(user).stream().map(TicketsResponseDto::of).collect(Collectors.toList());

        if(list.isEmpty()) {
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("ticket.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return list;
    }

    public void invalidateTicket(Long id){
        var ticket = findById(id);
        ticket.setExpired(true);
        ticketsRepository.save(ticket);
    }




}
