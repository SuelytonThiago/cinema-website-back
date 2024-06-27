package com.example.project.rest.services;

import com.example.project.domain.entities.Chairs;
import com.example.project.domain.repositories.ChairRepository;
import com.example.project.rest.dto.ChairRequestDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChairService {

    private final ChairRepository chairRepository;
    private final UsersService usersService;
    private  final SessionsService sessionsService;

    public Chairs findById(Long id){
        return chairRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExceptions("this chair is not found"));
    }

    public void addChair(ChairRequestDto dto){
        var session = sessionsService.findById(dto.getSessionId());
        var user = usersService.findById(dto.userId);
        chairRepository.save(Chairs.of(dto,session,user));
    }
}
