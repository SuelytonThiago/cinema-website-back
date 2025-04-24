package com.example.project.rest.service;


import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Reviews;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.ReviewsRepository;
import com.example.project.rest.dto.ReviewRequestDto;
import com.example.project.rest.dto.ReviewsResponseDto;
import com.example.project.rest.services.JwtService;
import com.example.project.rest.services.MovieService;
import com.example.project.rest.services.ReviewService;
import com.example.project.rest.services.UsersService;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewsRepository reviewsRepository;
    @Mock
    private UsersService usersService;
    @Mock
    private MovieService movieService;
    @Mock
    private JwtService jwtService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private ReviewService reviewService;

    private Users user;
    private Reviews review;
    private Movies movies;
    private ReviewRequestDto dto;
    private ReviewsResponseDto responseDto;

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

        review = new Reviews();
        review.setRating(5);
        review.setDate(LocalDate.now());
        review.setUser(user);
        review.setMovie(movies);
        review.setComment("asdasdasdasdasd");
        review.setId(1L);


        dto = new ReviewRequestDto();
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setMovieId(movies.getId());

        responseDto = ReviewsResponseDto.of(review);
    }

    @Test
    void testAddReview() {
        var request = mock(HttpServletRequest.class);
        given(jwtService.getClaimId(any(HttpServletRequest.class))).willReturn(user.getId());
        given(usersService.findById(anyLong())).willReturn(user);
        given(movieService.findById(anyLong())).willReturn(movies);
        given(reviewsRepository.findByUserAndMovie(any(Users.class), any(Movies.class))).willReturn(Optional.empty());
        given(reviewsRepository.save(any(Reviews.class))).willReturn(review);

        var response = reviewService.addReview(dto,request);

        assertThat(response).isEqualTo(responseDto);
        verify(jwtService).getClaimId(any(HttpServletRequest.class));
        verify(usersService).findById(anyLong());
        verify(movieService).findById(anyLong());
        verify(reviewsRepository).findByUserAndMovie(any(Users.class),any(Movies.class));
        verify(reviewsRepository).save(any(Reviews.class));
        verifyNoMoreInteractions(jwtService);
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(reviewsRepository);


    }

    @Test
    void testAddReviewAlreadyExistsReview() {
        var request = mock(HttpServletRequest.class);
        given(jwtService.getClaimId(any(HttpServletRequest.class))).willReturn(user.getId());
        given(usersService.findById(anyLong())).willReturn(user);
        given(movieService.findById(anyLong())).willReturn(movies);
        given(reviewsRepository.findByUserAndMovie(any(Users.class), any(Movies.class))).willReturn(Optional.of(review));

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> reviewService.addReview(dto,request))
                .withMessage(
                        messageSource.getMessage("review.service.error.reviewAlreadyAdded", null, LocaleContextHolder.getLocale())
                );


        verify(jwtService).getClaimId(any(HttpServletRequest.class));
        verify(usersService).findById(anyLong());
        verify(movieService).findById(anyLong());
        verify(reviewsRepository).findByUserAndMovie(any(Users.class),any(Movies.class));
        verifyNoMoreInteractions(jwtService);
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(reviewsRepository);
    }



    @Test
    void testAddReviewWithInvalidRating() {
        dto.setRating(7);
        var request = mock(HttpServletRequest.class);
        given(jwtService.getClaimId(any(HttpServletRequest.class))).willReturn(user.getId());
        given(usersService.findById(anyLong())).willReturn(user);
        given(movieService.findById(anyLong())).willReturn(movies);
        given(reviewsRepository.findByUserAndMovie(any(Users.class), any(Movies.class))).willReturn(Optional.of(review));

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> reviewService.addReview(dto,request))
                .withMessage(
                        messageSource.getMessage("review.service.error.constraintViolationException", null, LocaleContextHolder.getLocale())
                );

        verify(jwtService).getClaimId(any(HttpServletRequest.class));
        verify(usersService).findById(anyLong());
        verify(movieService).findById(anyLong());
        verify(reviewsRepository).findByUserAndMovie(any(Users.class),any(Movies.class));
        verifyNoMoreInteractions(jwtService);
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(reviewsRepository);
    }


    @Test
    void testFindById() {
        given(reviewsRepository.findById(anyLong())).willReturn(Optional.of(review));

        var response = reviewService.findById(review.getId());

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(review);
        verify(reviewsRepository).findById(anyLong());
        verifyNoMoreInteractions(reviewsRepository);

    }

    @Test
    void testFindByIdWithInvalidId() {
        given(reviewsRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> reviewService.findById(review.getId()))
                .withMessage(
                        messageSource.getMessage("review.service.error.notFound", null, LocaleContextHolder.getLocale())
                );

        verify(reviewsRepository).findById(anyLong());
        verifyNoMoreInteractions(reviewsRepository);
    }

    @Test
    void testFindByUserAndMovie() {
        given(usersService.findById(anyLong())).willReturn(user);
        given(movieService.findById(anyLong())).willReturn(movies);
        given(reviewsRepository.findByUserAndMovie(any(Users.class),any(Movies.class))).willReturn(Optional.of(review));

        var response = reviewService.findByUserAndMovie(user.getId(),movies.getId());

        assertThat(response).isEqualTo(responseDto);
        verify(usersService).findById(anyLong());
        verify(movieService).findById(anyLong());
        verify(reviewsRepository).findByUserAndMovie(any(Users.class),any(Movies.class));
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(reviewsRepository);
    }

    @Test
    void testFindByUserAndMovieWithReviewNotFoundException() {
        given(usersService.findById(anyLong())).willReturn(user);
        given(movieService.findById(anyLong())).willReturn(movies);
        given(reviewsRepository.findByUserAndMovie(any(Users.class),any(Movies.class))).willReturn(Optional.empty());

       assertThatExceptionOfType(CustomException.class)
               .isThrownBy(() -> reviewService.findByUserAndMovie(user.getId(),movies.getId()))
               .withMessage(
                       messageSource.getMessage("review.service.error.userReviewNotFound", null, LocaleContextHolder.getLocale())
               );

        verify(usersService).findById(anyLong());
        verify(movieService).findById(anyLong());
        verify(reviewsRepository).findByUserAndMovie(any(Users.class),any(Movies.class));
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(reviewsRepository);
    }


    @Test
    void testUpdateReview() {
        given(reviewsRepository.findById(anyLong())).willReturn(Optional.of(review));
        given(reviewsRepository.save(any(Reviews.class))).willReturn(review);

        var response = reviewService.updateReview(dto, review.getId());

        assertThat(response).isEqualTo(responseDto);
        verify(reviewsRepository).findById(anyLong());
        verify(reviewsRepository).save(any(Reviews.class));
        verifyNoMoreInteractions(reviewsRepository);
    }


}
