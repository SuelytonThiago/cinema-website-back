package com.example.project.rest.services;

import com.example.project.domain.entities.Chairs;
import com.example.project.domain.entities.Sessions;
import com.example.project.domain.repositories.ChairRepository;
import com.example.project.rest.dto.ChairRequestDto;
import com.example.project.rest.services.exceptions.AlreadyExistsExceptions;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChairService {

    private final ChairRepository chairRepository;
    private final UsersService usersService;
    private  final SessionsService sessionsService;
    private final JwtService jwtService;

    public Chairs findById(Long id){
        return chairRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExceptions("this chair is not found"));
    }

    @Transactional
    public void addChair(ChairRequestDto dto, HttpServletRequest request){
        var session = sessionsService.findById(dto.getSessionId());

        var userId = jwtService.getClaimId(request);
        var user = usersService.findById(userId);
        verifyIfChairIsReserved(dto.getName(), session);

        var chair = Chairs.of(dto,session,user);
        chairRepository.save(Chairs.of(dto,session,user));

        session.getChairs().add(chair);
        sessionsService.saveSession(session);
    }

    public void verifyIfChairIsReserved(String name ,Sessions session){
        chairRepository.findByNameAndSession(name, session)
                .ifPresent(e -> {
                    throw new AlreadyExistsExceptions("the seat has already been reserved");
                });
    }
}
