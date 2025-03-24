package com.example.project.rest.controllers;
import com.example.project.rest.dto.AddCategoryToMovieRequestDto;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/movies")
@SecurityRequirement(name = "bearerAuth")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private Validator validator;


    @PostMapping("/add")
    @Operation(summary = "add a new movie")
    public ResponseEntity<Map<String, String>> addMovie(@RequestParam("movie") String movieJson,
                                                        @RequestParam("fileImg") MultipartFile fileImg,
                                                        @RequestParam("backgroundCover") MultipartFile backgroundCover,
                                                        HttpServletRequest request) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        MovieRequestDto dto = objectMapper.readValue(movieJson, MovieRequestDto.class);

        Set<ConstraintViolation<MovieRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<MovieRequestDto> violation : violations) {

                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        movieService.createMovie(fileImg,backgroundCover, dto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add/category")
    @Operation(summary = "add a category to the movie")
    public ResponseEntity<Void> addCategoryToFilm(@RequestBody @Valid AddCategoryToMovieRequestDto dto, HttpServletRequest request){
        movieService.addCategoryToMovie(dto, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "search all movies")
    public ResponseEntity<List<MovieResponseDto>> findAll(){
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/search")
    @Operation(summary = "search movie by name")
    public ResponseEntity<List<MovieResponseDto>> search(@RequestParam String name){
        return ResponseEntity.ok(movieService.findMovieByName(name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "search movie by id")
    public ResponseEntity<MovieResponseDto> findMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findMovieById(id));
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "search movies by category")
    public ResponseEntity<List<MovieResponseDto>> findByCategory(@PathVariable Long id){
        return ResponseEntity.ok(movieService.findAllByCategory(id));
    }

    @GetMapping("/randomMovies")
    @Operation(summary = "Get 10 random movies from the database")
    public ResponseEntity<List<MovieResponseDto>> get10RandomMovies() {
        return ResponseEntity.ok(movieService.getRandomMovies());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "update movie data")
    public ResponseEntity<Void> updateMovie(@RequestBody @Valid MovieRequestDto dto,
                                            @PathVariable Long id,
                                            HttpServletRequest request){
        movieService.updateMovieData(id,dto, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "delete the movie")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id, HttpServletRequest request){
        movieService.deleteMovie(id, request);
        return ResponseEntity.noContent().build();
    }
}
