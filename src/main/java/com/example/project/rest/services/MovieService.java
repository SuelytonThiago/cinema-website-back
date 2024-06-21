package com.example.project.rest.services;

import com.example.project.domain.entities.Movies;
import com.example.project.domain.repositories.MovieRepository;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final MovieRepository movieRepository;

    public void createMovie(MovieRequestDto dto){
        movieRepository.save(Movies.of(dto));
    }

    @Transactional
    public List<MovieResponseDto> findMovieByName(String name){
        return movieRepository.findByNameLike(name).stream().map(MovieResponseDto::of)
                .collect(Collectors.toList());
    }

    public Movies findById(Long id){
        return movieRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions("this movie is not found")
        );
    }

    @Transactional
    public void updateMovieData(Long id,MovieRequestDto dto){
        var movie = findById(id);
        updateData(dto, movie);
        movieRepository.save(movie);
    }

    public void deleteMovie(Long id){
        var movie = findById(id);
        movieRepository.delete(movie);
    }

    private void updateData(MovieRequestDto dto, Movies movie) {
        movie.setName(dto.getName());
        movie.setDescription(dto.getDescription());
        movie.setReleaseData(LocalDate.parse(dto.getReleaseData(), formatter));
    }

}
