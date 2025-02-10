package com.example.project.rest.controllers;
import com.example.project.rest.dto.AddCategoryToMovieRequestDto;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@SecurityRequirement(name = "bearerAuth")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/add")
    @Operation(summary = "add a new movie")
    public ResponseEntity<Void> addMovie(@RequestBody @Valid MovieRequestDto dto){
        movieService.createMovie(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add/category")
    @Operation(summary = "add a category to the movie")
    public ResponseEntity<Void> addCategoryToFilm(@RequestBody AddCategoryToMovieRequestDto dto){
        movieService.addCategoryToMovie(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "search movie by name")
    public ResponseEntity<List<MovieResponseDto>> findByName(@RequestParam String name){
        return ResponseEntity.ok(movieService.findMovieByName(name));
    }

    @GetMapping
    @Operation(summary = "search all movies")
    public ResponseEntity<List<MovieResponseDto>> findAll(){
        return ResponseEntity.ok(movieService.findAll());
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
    public ResponseEntity<Void> updateMovie(@RequestBody @Valid MovieRequestDto dto,@PathVariable Long id){
        movieService.updateMovieData(id,dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "delete the movie")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
