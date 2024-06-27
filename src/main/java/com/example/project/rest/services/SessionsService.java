package com.example.project.rest.services;

import com.example.project.domain.entities.Sessions;
import com.example.project.domain.repositories.SessionsRepository;
import com.example.project.rest.dto.SessionRequestDto;
import com.example.project.rest.dto.SessionResponseDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionsService {

    private final SessionsRepository sessionsRepository;
    private final MovieService movieService;

    public Sessions findById(Long id) {
        return sessionsRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions("this room is not found")
        );
    }

    public void createSession(SessionRequestDto dto){
        var movie = movieService.findById(dto.getMovieId());
        sessionsRepository.save(Sessions.of(dto,movie));

    }

    public SessionResponseDto getSessionInformation(Long id){
        return SessionResponseDto.of(findById(id));
    }

    public void updateSession(Long id,SessionRequestDto dto){
        var session  = findById(id);
        updateData(session,dto);
        sessionsRepository.save(session);
    }

    private void updateData(Sessions sessions, SessionRequestDto dto){
        sessions.setName(dto.getName());
        sessions.setDateStart(Sessions.convertStringToLocalDateTime(dto.getDateStart()));
        sessions.setDateEnd(Sessions.convertStringToLocalDateTime(dto.getDateEnd()));
        if(dto.getMovieId() != null){
            var movie = movieService.findById(dto.getMovieId());
            sessions.setMovie(movie);
        }
    }
}
