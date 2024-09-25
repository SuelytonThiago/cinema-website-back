package com.example.project.rest.services;

import com.example.project.domain.entities.Sessions;
import com.example.project.domain.repositories.SessionsRepository;
import com.example.project.rest.dto.ChairResponseDto;
import com.example.project.rest.dto.SessionRequestDto;
import com.example.project.rest.dto.SessionResponseDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SessionsService {

    private final SessionsRepository sessionsRepository;
    private final MovieService movieService;

    public void saveSession(Sessions sessions){
        sessionsRepository.save(sessions);
    }

    public Sessions findById(Long id) {
        return sessionsRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions("this session is not found")
        );
    }

    @Transactional
    public List<SessionResponseDto> findSessionsByMovie(Long movieId){
        var movie = movieService.findById(movieId);
        return sessionsRepository.findAllWithDateAfterAndMovie(LocalDateTime.now(), movie).stream()
                .map(SessionResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createSession(SessionRequestDto dto){
        var movie = movieService.findById(dto.getMovieId());
        sessionsRepository.save(Sessions.of(dto,movie));

    }

    public SessionResponseDto getSessionInformation(Long id){
        return SessionResponseDto.of(findById(id));
    }

    public List<SessionResponseDto> getAllSessions(){
        return sessionsRepository.findAllWithDateAfter(LocalDateTime.now())
                .stream().map(SessionResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
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
