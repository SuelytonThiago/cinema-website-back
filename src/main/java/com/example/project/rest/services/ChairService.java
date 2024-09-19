package com.example.project.rest.services;
import com.example.project.domain.entities.Chairs;
import com.example.project.domain.entities.Sessions;
import com.example.project.domain.repositories.ChairRepository;
import com.example.project.rest.dto.ChairResponseDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChairService {

    private final ChairRepository chairRepository;
    private final SessionsService sessionsService;

    public Chairs findById(Long id){
        return chairRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExceptions("this chair is not found"));
    }


    @Transactional
    public List<ChairResponseDto> getAllChairs(Long sessionId){
        List<ChairResponseDto> list = new ArrayList<>();
        var session = sessionsService.findById(sessionId);
        for(int i=0;i<session.getChairsAvailable().length;i++){
            var chair = new ChairResponseDto();
            chair.setChairNumber(i + 1);
            chair.setAvailable(session.isChairAvailable(i));
            list.add(chair);
        }
        return list;
    }

    public Chairs saveChair(Chairs chairs){
        return chairRepository.save(chairs);
    }

}
