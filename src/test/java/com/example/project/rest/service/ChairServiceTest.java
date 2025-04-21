package com.example.project.rest.service;

import com.example.project.domain.entities.Chairs;
import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Sessions;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.ChairRepository;
import com.example.project.rest.dto.ChairResponseDto;
import com.example.project.rest.services.ChairService;
import com.example.project.rest.services.SessionsService;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChairServiceTest {

    @Mock
    private ChairRepository repository;
    @Mock
    private SessionsService sessionsService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private ChairService chairService;

    private Chairs chair;
    private Sessions session;
    private Movies movie;
    private Users users;
    private ChairResponseDto chairResponseDto;

    @BeforeEach
    public void setUp() {
        users = new Users(1L, "nasdasd","Asdasdsa","ASdasdasd","SADASdasd","asdasdasd");
        movie = new Movies(1L,"filme1", "sadasdsadasd" , LocalDate.now(), "L");
        session = new Sessions(1L, "sala1", LocalDateTime.now(),LocalDateTime.now().plusMinutes(10),movie);
        chair = new Chairs(1L, 1, users, session);
        chairResponseDto = new ChairResponseDto(chair.getNumber(),true);
        session.getChairs().add(chair);


    }


    @Test
    void testFindById() {
        given(repository.findById(anyLong())).willReturn(Optional.of(chair));

        var response = chairService.findById(chair.getId());

        assertThat(response).isEqualTo(chair);
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindByIdIfNotFound() {
        var id = 5L;
        given(repository.findById(anyLong())).willReturn(Optional.empty());


        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> chairService.findById(id))
                .withMessage(
                        messageSource.getMessage("chair.service.error.notRegistered", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetAllChairs() {
        given(sessionsService.findById(anyLong())).willReturn(session);

        var list = chairService.getAllChairs(session.getId());

        assertThat(list.size()).isEqualTo(80);

        verify(sessionsService).findById(anyLong());
        verifyNoMoreInteractions(sessionsService);
    }

    @Test
    void testGetAllChairsIfNotFoundSession() {
        var id = 5L;
        given(sessionsService.findById(anyLong())).willReturn(null);

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> chairService.getAllChairs(id))
                .withMessage(
                        messageSource.getMessage("session.service.error.notFound", null, LocaleContextHolder.getLocale())
                );

        verify(sessionsService).findById(anyLong());
        verifyNoMoreInteractions(sessionsService);
    }

    @Test
    void testSaveChair() {
        chairService.saveChair(chair);
        verify(repository).save(any(Chairs.class));
        verifyNoMoreInteractions(repository);
    }

}
