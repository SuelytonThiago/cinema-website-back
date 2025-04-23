package com.example.project.rest.service;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Roles;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.MovieRepository;
import com.example.project.rest.dto.AddCategoryToMovieRequestDto;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.CategoryService;
import com.example.project.rest.services.MovieService;
import com.example.project.rest.services.S3Service;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private MovieRepository repository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private  UsersService usersService;
    @Mock
    private  S3Service s3Service;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private MovieService movieService;

    private Movies movies;
    private Categories categorie1;
    private Categories categorie2;
    private Users adm;
    private Users user;
    private Roles admRole;
    private Roles userRole;
    private MovieRequestDto movieRequestDto;
    private AddCategoryToMovieRequestDto categoryRequest1;
    private AddCategoryToMovieRequestDto categoryRequest2;
    private MovieResponseDto movieResponseDto;

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

        movieResponseDto = MovieResponseDto.of(movies);

        categorie1 = new Categories(1L,"artes marciais");
        categorie2 = new Categories(2L,"drama");

        categoryRequest1 = new AddCategoryToMovieRequestDto(movies.getId(), "ação");
        categoryRequest2 = new AddCategoryToMovieRequestDto(movies.getId(), "artes marciais");

        movies.getCategories().add(categorie1);


        adm = new Users();
        user = new Users();

        adm.setId(1L);
        adm.setName("adm");
        adm.setEmail("adm@example.com");
        adm.setContactNumber("99940028922");
        adm.setCpf("61254591010");
        adm.setPassword(encoder.encode("Senha123"));
        adm.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.");

        user.setId(2L);
        user.setName("maria");
        user.setEmail("maria@example.com");
        user.setContactNumber("99940028922");
        user.setCpf("87466407030");
        user.setPassword(encoder.encode("Senha123"));
        user.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.jpg");

        admRole = new Roles(null,"ROLE_ADMIN");
        userRole = new Roles(null,"ROLE_USER");

        movieRequestDto = new MovieRequestDto("asdasdasd","sadasdasd","12/02/2002","L");
    }

    @Test
    void testCreateMovie() {
        MultipartFile imgFile = mock(MultipartFile.class);
        MultipartFile backgroundCover = mock(MultipartFile.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(s3Service.uploadFileImg(any(MultipartFile.class)))
                .willReturn(movies.getImageUrl())
                .willReturn(movies.getBackgroundCover());

        given(repository.save(any(Movies.class))).willReturn(movies);

        movieService.createMovie(imgFile, backgroundCover, movieRequestDto, request);

        verify(usersService).checkIfIsADM(any(HttpServletRequest.class));

        verify(s3Service, times(2)).uploadFileImg(any(MultipartFile.class));
        verify(repository,times(2)).save(any(Movies.class));

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(s3Service);
    }

    @Test
    void testCreateMovieWithDateTimeParseExceptionError() {
        movieRequestDto.setReleaseData("SAdasdsada");
        MultipartFile imgFile = mock(MultipartFile.class);
        MultipartFile backgroundCover = mock(MultipartFile.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> movieService.createMovie(imgFile,backgroundCover,movieRequestDto,request))
                .withMessage(
                        messageSource.getMessage("format.data.error", null, LocaleContextHolder.getLocale())
                );

        verifyNoInteractions(repository);
        verifyNoInteractions(s3Service);
        verify(usersService).checkIfIsADM(any(HttpServletRequest.class));
        verifyNoMoreInteractions(usersService);
    }

    @Test
    void testAddCategoryToMovie() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(categoryService.findByName(anyString())).willReturn(categorie2);
        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        movieService.addCategoryToMovie(categoryRequest1, request);

        verify(usersService).checkIfIsADM(request);
        verify(categoryService).findByName(anyString());
        verify(repository).findById(anyLong());
        verify(repository).save(any(Movies.class));
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testAddCategoryToMovieIfTheMovieAlreadyHasTheCategory() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(categoryService.findByName(anyString())).willReturn(categorie1);
        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> movieService.addCategoryToMovie(categoryRequest2,request))
                .withMessage(
                        messageSource.getMessage("movie.service.error.movieAlreadyAdded", null, LocaleContextHolder.getLocale())
                );
        verify(categoryService).findByName(anyString());
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindMovieByName() {
        given(repository.findByNameLike(anyString())).willReturn(Collections.singletonList(movies));

        var list = movieService.findMovieByName(movies.getName());

        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("name","description","classification")
                .contains(
                        tuple(movies.getName(),movies.getDescription(),movies.getClassification())
                );
        verify(repository).findByNameLike(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindMovieByNameIfReturnEmptyList() {
        var name = "asddasdasdas";

        given(repository.findByNameLike(anyString())).willReturn(Collections.emptyList());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> movieService.findMovieByName(name))
                .withMessage(
                        messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findByNameLike(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindAll() {
        given(repository.findAll()).willReturn(Collections.singletonList(movies));

        var list = movieService.findAll();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("name","description","classification")
                .contains(
                        tuple(movies.getName(),movies.getDescription(),movies.getClassification())
                );
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindAllIfReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> movieService.findAll())
                .withMessage(
                        messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindById() {
        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        var response = movieService.findById(movies.getId());

        assertThat(response).isEqualTo(movies);
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindByIdWithInvalidId() {
        var id = 5L;
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> movieService.findById(id))
                .withMessage(
                        messageSource.getMessage("movie.service.error.notFound", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindAllByCategory() {
        given(categoryService.findById(anyLong())).willReturn(categorie1);
        given(repository.findByCategories(any(Categories.class))).willReturn(Collections.singletonList(movies));

        var list = movieService.findAllByCategory(categorie1.getId());

        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("name","description","rating","releaseData","imageUrl","classification","backgroundCover")
                .contains(
                        tuple(
                            movieResponseDto.getName(),
                            movieResponseDto.getDescription(),
                            movieResponseDto.getRating(),
                            movieResponseDto.getReleaseData(),
                            movieResponseDto.getImageUrl(),
                            movieResponseDto.getClassification(),
                            movieResponseDto.getBackgroundCover())
                );
        assertThat(list.getFirst().getCategories()).extracting("name")
                .containsExactly(
                        categorie1.getName()
                );

        verify(categoryService).findById(anyLong());
        verify(repository).findByCategories(any(Categories.class));
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(repository);
    }


    @Test
    void testFindAllByCategoryIfReturnEmptyList() {
        given(categoryService.findById(anyLong())).willReturn(categorie2);
        given(repository.findByCategories(any(Categories.class))).willReturn(Collections.emptyList());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> movieService.findAllByCategory(categorie2.getId()))
                .withMessage(
                        messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
                );

        verify(categoryService).findById(anyLong());
        verify(repository).findByCategories(any(Categories.class));
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetRandomMovies() {
        given(repository.findRandomMovieIds(anyInt())).willReturn(Collections.singletonList(movies.getId()));
        given(repository.findMoviesByIds(anyList())).willReturn(Collections.singletonList(movies));

        var list = movieService.getRandomMovies();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("name","description","rating","releaseData","imageUrl","classification","backgroundCover")
                .contains(
                        tuple(
                                movieResponseDto.getName(),
                                movieResponseDto.getDescription(),
                                movieResponseDto.getRating(),
                                movieResponseDto.getReleaseData(),
                                movieResponseDto.getImageUrl(),
                                movieResponseDto.getClassification(),
                                movieResponseDto.getBackgroundCover())
                );
        assertThat(list.getFirst().getCategories()).extracting("name")
                .containsExactly(
                        categorie1.getName()
                );

        verify(repository).findRandomMovieIds(anyInt());
        verify(repository).findMoviesByIds(anyList());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetRandomMoviesIfReturnEmptyList() {
        given(repository.findRandomMovieIds(anyInt())).willReturn(Collections.singletonList(movies.getId()));
        given(repository.findMoviesByIds(anyList())).willReturn(Collections.emptyList());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> movieService.getRandomMovies())
                .withMessage(
                        messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findRandomMovieIds(anyInt());
        verify(repository).findMoviesByIds(anyList());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testUpdateMovieData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile imgFile = mock(MultipartFile.class);
        MultipartFile backgroundCover = mock(MultipartFile.class);

        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        movieService.updateMovieData(imgFile,backgroundCover,movies.getId(),movieRequestDto,request);

        verify(usersService).checkIfIsADM(any(HttpServletRequest.class));
        verify(s3Service, times(2)).uploadFileImg(any(MultipartFile.class));
        verify(repository).findById(anyLong());
        verify(repository).save(any(Movies.class));
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(s3Service);
        verifyNoMoreInteractions(repository);

    }

    @Test
    void testUpdateMovieDateTimeParseException() {
        movieRequestDto.setReleaseData("ASdasdsaasdadsasd");

        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile imgFile = mock(MultipartFile.class);
        MultipartFile backgroundCover = mock(MultipartFile.class);


        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        assertThatExceptionOfType(CustomException.class)
               .isThrownBy(() -> movieService.updateMovieData(imgFile,backgroundCover,movies.getId(),movieRequestDto,request))
                       .withMessage(
                               messageSource.getMessage("format.data.error", null, LocaleContextHolder.getLocale())
                       );

        verify(usersService).checkIfIsADM(any(HttpServletRequest.class));
        verify(s3Service, times(2)).uploadFileImg(any(MultipartFile.class));
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(s3Service);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testUpdateMovieDataWithNullImgMultpartFile() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile imgFile = null;
        MultipartFile backgroundCover = mock(MultipartFile.class);

        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        movieService.updateMovieData(imgFile,backgroundCover,movies.getId(),movieRequestDto,request);

        verify(s3Service,times(1)).uploadFileImg(any(MultipartFile.class));
        verifyNoMoreInteractions(s3Service);
        verify(repository).findById(anyLong());
        verify(repository).save(any(Movies.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testUpdateMovieDataWithNullBackgroundCoverMultpartFile() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile imgFile = mock(MultipartFile.class);;
        MultipartFile backgroundCover = null;

        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        movieService.updateMovieData(imgFile,backgroundCover,movies.getId(),movieRequestDto,request);

        verify(s3Service,times(1)).uploadFileImg(any(MultipartFile.class));
        verifyNoMoreInteractions(s3Service);
        verify(repository).findById(anyLong());
        verify(repository).save(any(Movies.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testUpdateMovieDataWithNullBackgroundCoverMultpartFileAndNullImgMultpartFile() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MultipartFile imgFile = null;
        MultipartFile backgroundCover = null;

        given(repository.findById(anyLong())).willReturn(Optional.of(movies));

        movieService.updateMovieData(imgFile,backgroundCover,movies.getId(),movieRequestDto,request);

        verifyNoInteractions(s3Service);
        verify(repository).findById(anyLong());
        verify(repository).save(any(Movies.class));
        verifyNoMoreInteractions(repository);

    }
}
