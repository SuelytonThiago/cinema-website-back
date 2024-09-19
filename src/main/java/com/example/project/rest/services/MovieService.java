package com.example.project.rest.services;

import com.example.project.domain.entities.Movies;
import com.example.project.domain.repositories.MovieRepository;
import com.example.project.rest.dto.AddCategoryToMovieRequestDto;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final CategoryService categoryService;
    private final MovieRepository movieRepository;

    public void createMovie(MovieRequestDto dto){
        movieRepository.save(Movies.of(dto));
    }

    @Transactional
    public void addCategoryToMovie(AddCategoryToMovieRequestDto dto){
        var category = categoryService.findByName(dto.getCategoryName());
        var movie = findById(dto.getMovieId());
        if(movie.getCategories().contains(category)){
            throw new CustomException("this category already added to movie");
        }
        movie.getCategories().add(category);
        movieRepository.save(movie);

    }

    @Transactional
    public Page<Movies> findMovieByName(String name, int page, int size){
        var pag = PageRequest.of(page,size);
        return movieRepository.findByNameContainingIgnoreCase(name, pag);
    }

    public List<MovieResponseDto> findAll(){
        return movieRepository.findAll()
                .stream()
                .map(MovieResponseDto::of)
                .collect(Collectors.toList());
    }

    public Movies findById(Long id){
        return movieRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions("this movie is not found")
        );
    }

    public MovieResponseDto findMovieById(Long id){
        return MovieResponseDto.of(movieRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions("this movie is not found")
        ));
    }

    public List<MovieResponseDto> findAllByCategory(Long id){
        var category = categoryService.findById(id);
        return movieRepository.findByCategories(category)
                .stream()
                .map(MovieResponseDto::of)
                .toList();
    }

    @Transactional
    public void updateMovieData(Long id,MovieRequestDto dto){
        var movie = findById(id);
        updateData(dto, movie);
        movieRepository.save(movie);
    }

    @Transactional
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
