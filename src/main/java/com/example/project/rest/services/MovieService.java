package com.example.project.rest.services;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.project.domain.entities.Movies;
import com.example.project.domain.repositories.MovieRepository;
import com.example.project.rest.dto.AddCategoryToMovieRequestDto;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final CategoryService categoryService;
    private final MovieRepository movieRepository;
    private final MessageSource messageSource;
    private final UsersService usersService;
    private final S3Service s3Service;

    @Transactional
    public void createMovie(MultipartFile imgFile,MultipartFile backgroundCover, MovieRequestDto dto,HttpServletRequest request){
        try{
            usersService.checkIfIsADM(request);
            var movie = movieRepository.save(Movies.of(dto));

            var imgUrl = s3Service.uploadFileImg(imgFile);
            var backgroundImg = s3Service.uploadFileImg(backgroundCover);
            movie.setBackgroundCover(backgroundImg);
            movie.setImageUrl(imgUrl);
            movieRepository.save(movie);

        } catch(DateTimeParseException e) {
            throw new CustomException(
                    messageSource.getMessage("format.data.error", null, LocaleContextHolder.getLocale())
            );
        }

    }

    @Transactional
    public void addCategoryToMovie(AddCategoryToMovieRequestDto dto, HttpServletRequest request){
        usersService.checkIfIsADM(request);
        var category = categoryService.findByName(dto.getCategoryName());
        var movie = findById(dto.getMovieId());
        if(movie.getCategories().contains(category)){

            throw new CustomException(
                    messageSource.getMessage("movie.service.error.movieAlreadyAdded", null, LocaleContextHolder.getLocale())
            );
        }

        movie.getCategories().add(category);
        movieRepository.save(movie);

    }

    @Transactional
    public List<MovieResponseDto> findMovieByName(String name){
        var list = movieRepository.findByNameLike(name)
                .stream()
                .map(MovieResponseDto::of).collect(Collectors.toList());
        if(list.isEmpty()) {

            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return list;
    }


    public List<MovieResponseDto> findAll(){
        var list = movieRepository.findAll()
                .stream()
                .map(MovieResponseDto::of)
                .collect(Collectors.toList());

        if(list.isEmpty()) {
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return list;
    }

    public Movies findById(Long id){
        return movieRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("movie.service.error.notFound", null, LocaleContextHolder.getLocale())
                )
        );
    }

    public MovieResponseDto findMovieById(Long id){
        return MovieResponseDto.of(movieRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("movie.service.error.notFound", null, LocaleContextHolder.getLocale())
                )
        ));
    }

    @Transactional
    public List<MovieResponseDto> findAllByCategory(Long id){
        var category = categoryService.findById(id);
        var list =  movieRepository.findByCategories(category)
                .stream()
                .map(MovieResponseDto::of)
                .toList();
        if(list.isEmpty()) {
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return list;
    }

    @Transactional
    public List<MovieResponseDto> getRandomMovies() {
        var randomIds = movieRepository.findRandomMovieIds(10);
        var randomMovies = movieRepository.findMoviesByIds(randomIds)
                .stream()
                .map(MovieResponseDto::of)
                .toList();

        if(randomMovies.isEmpty()){
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("movie.service.error.emptyList", null, LocaleContextHolder.getLocale())
            );
        }
        return randomMovies;

    }

    @Transactional
    public void updateMovieData(MultipartFile imgFile,MultipartFile backgroundCover, Long id, MovieRequestDto dto, HttpServletRequest request){
        try {
            usersService.checkIfIsADM(request);
            var movie = findById(id);

            if (imgFile != null && !imgFile.isEmpty()) {
                var imgUrl = s3Service.uploadFileImg(imgFile);
                movie.setImageUrl(imgUrl);
            }

            if (backgroundCover != null && !backgroundCover.isEmpty()) {
                var backgroundImg = s3Service.uploadFileImg(backgroundCover);
                movie.setBackgroundCover(backgroundImg);
            }

            updateData(dto, movie);
            movieRepository.save(movie);

        } catch(DateTimeParseException e) {
            throw new CustomException(
                    messageSource.getMessage("format.data.error", null, LocaleContextHolder.getLocale())
            );
        }
    }

    @Transactional
    public void deleteMovie(Long id, HttpServletRequest request){
        usersService.checkIfIsADM(request);
        var movie = findById(id);
        movieRepository.delete(movie);
    }

    private void updateData(MovieRequestDto dto, Movies movie) {
        movie.setName(dto.getName());
        movie.setDescription(dto.getDescription());
        movie.setReleaseData(LocalDate.parse(dto.getReleaseData(), formatter));
        movie.setClassification(dto.getClassification());
    }

}
