package com.example.project.rest.service;

import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Roles;
import com.example.project.domain.entities.Sessions;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.SessionsRepository;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.SessionResponseDto;
import com.example.project.rest.services.MovieService;
import com.example.project.rest.services.SessionsService;
import com.example.project.rest.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionsRepository sessionsRepository;
    @Mock
    private MovieService movieService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private UsersService usersService;
    @InjectMocks
    private SessionsService sessionsService;

    private Users user, adm;
    private Sessions session;
    private Movies movies;
    private Roles admRole, userRole;
    private SessionResponseDto sessionResponseDto;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @BeforeEach
    void setUp() {
        movies = new Movies();
        movies.setId(1L);
        movies.setImageUrl("https://play-lh.googleusercontent.com/9pxN3jrTbT04lAxYu5BL9kMvUmeR2WoMyA3lP78AKDSj6Z9hY8zXF1IaJ3tkxgUqvZE");
        movies.setName("Velozes e furiosos 1");
        movies.setDescription("Eles são os pilotos dos automóveis mais envenenados de Los Angeles. Em alucinantes rachas ilegais, eles rasgam as ruas em busca de emoção adrenalina, sem medir esforços nem perigos para conquistar sempre o primeiro lugar. Audazes e irresponsáveis, alguns destes corredores são suspeitos deformar uma quadrilha especializada em roubos de cargas de caminhões. Para investigar o caso, a polícia infiltra um piloto entre os marginais.");
        movies.setReleaseData(LocalDate.parse("28/09/2001", formatter));
        movies.setClassification("18");
        movies.setBackgroundCover("https://www.google.com/url?sa=i&url=https%3A%2F%2Folhardigital.com.br%2F2023%2F06%2F08%2Fcinema-e-streaming%2Fvelozes-e-furiosos-saiba-a-ordem-certa-dos-filmes%2F&psig=AOvVaw2AkzjlhZT-EGIHiGB__qwZ&ust=1745276360614000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJipsJ_b54wDFQAAAAAdAAAAABAE");

        user = new Users();
        user.setId(1L);
        user.setName("maria");
        user.setEmail("maria@example.com");
        user.setContactNumber("99940028922");
        user.setCpf("87466407030");
        user.setPassword("Senha123");
        user.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.jpg");

        adm = new Users();
        adm.setName("adm");
        adm.setEmail("adm@example.com");
        adm.setContactNumber("99940028922");
        adm.setCpf("61254591010");
        adm.setPassword("Senha123");
        adm.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.");

        admRole = new Roles(null,"ROLE_ADMIN");
        userRole = new Roles(null,"ROLE_USER");

        user.getRoles().add(userRole);
        adm.getRoles().add(admRole);

        session = new Sessions();
        session.setMovie(movies);
        session.setId(1L);
        session.setDateStart(LocalDateTime.now());
        session.setDateEnd(LocalDateTime.now().plusMinutes(30));
        session.setName("session 1");

        sessionResponseDto = SessionResponseDto.of(session);

    }


    @Test
    void testSaveSession() {
        sessionsService.saveSession(session);
        verify(sessionsRepository).save(any(Sessions.class));
        verifyNoMoreInteractions(sessionsRepository);
    }

    @Test
    void testFindById() {

    }

    @Test
    void testFindSessionsByMovie() {
        given(movieService.findById(anyLong())).willReturn(movies);
        given(sessionsRepository.findAllWithDateAfterAndMovie(any(LocalDateTime.class), any(Movies.class))).willReturn(Collections.singletonList(session));

        var list = sessionsService.findSessionsByMovie(movies.getId());


        assertThat(list.size()).isEqualTo(1);
        assertThat(list)
                .extracting(
                        "id",
                        "movieName",
                        "sessionName",
                        "duration",
                        "movieSynopsis",
                        "rating",
                        "imageUrl",
                        "dateStart")
                .contains(
                        tuple(sessionResponseDto.getId(),
                                sessionResponseDto.getMovieName(),
                                sessionResponseDto.getSessionName(),
                                sessionResponseDto.getDuration(),
                                sessionResponseDto.getMovieSynopsis(),
                                sessionResponseDto.getRating(),
                                sessionResponseDto.getImageUrl(),
                                sessionResponseDto.getDateStart())
                );
        verify(movieService).findById(anyLong());
        verify(sessionsRepository).findAllWithDateAfterAndMovie(any(LocalDateTime.class), any(Movies.class));
        verifyNoMoreInteractions(sessionsRepository);
        verifyNoMoreInteractions(movieService);

    }

    @Test
    void testFindSessionsByMovieReturnEmptyList() {

    }

}
