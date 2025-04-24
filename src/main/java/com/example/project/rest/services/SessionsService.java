package com.example.project.rest.services;

import com.example.project.domain.entities.Sessions;
import com.example.project.domain.repositories.SessionsRepository;
import com.example.project.rest.dto.SessionRequestDto;
import com.example.project.rest.dto.SessionResponseDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SessionsService {

    private final SessionsRepository sessionsRepository;
    private final MovieService movieService;
    private final MessageSource messageSource;
    private final UsersService usersService;

    public void saveSession(Sessions sessions){
        sessionsRepository.save(sessions);
    }

    public Sessions findById(Long id) {
        return sessionsRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("session.service.error.notFound", null, LocaleContextHolder.getLocale())
                )
        );
    }

    @Transactional
    public List<SessionResponseDto> findSessionsByMovie(Long movieId){
        var movie = movieService.findById(movieId);
        var list = sessionsRepository.findAllWithDateAfterAndMovie(LocalDateTime.now(), movie).stream()
                .map(SessionResponseDto::of)
                .collect(Collectors.toList());
        if(list.isEmpty()) {
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("session.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return list;
    }

    @Transactional
    public SessionResponseDto createSession(SessionRequestDto dto, HttpServletRequest request){
        usersService.checkIfIsADM(request);
        try {
            var movie = movieService.findById(dto.getMovieId());
            var session = sessionsRepository.save(Sessions.of(dto,movie));

            if(session.getDateStart().isBefore(LocalDateTime.now())){

                throw new CustomException(
                        messageSource.getMessage("session.service.error.addBeforeLocalDateNow", null, LocaleContextHolder.getLocale())
                );
            }
            return SessionResponseDto.of(session);
        } catch(DateTimeParseException e) {
            throw new CustomException(
                    messageSource.getMessage("session.service.error.invalidDate", null, LocaleContextHolder.getLocale())
            );
        }
    }

    public SessionResponseDto getSessionInformation(Long id){
        return SessionResponseDto.of(findById(id));
    }

    public List<SessionResponseDto> getAllSessions(){
        var list =  sessionsRepository.findAllWithDateAfter(LocalDateTime.now())
                .stream().map(SessionResponseDto::of)
                .collect(Collectors.toList());
        if(list.isEmpty()) {

            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("session.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return list;
    }
}
