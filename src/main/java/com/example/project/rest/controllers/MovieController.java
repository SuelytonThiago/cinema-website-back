package com.example.project.rest.controllers;
import com.example.project.rest.dto.AddCategoryToMovieRequestDto;
import com.example.project.rest.dto.MovieRequestDto;
import com.example.project.rest.dto.MovieResponseDto;
import com.example.project.rest.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/add")
    public ResponseEntity<Void> addMovie(@RequestBody @Valid MovieRequestDto dto){
        movieService.createMovie(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add/category")
    public ResponseEntity<Void> addCategoryToFilm(@RequestBody AddCategoryToMovieRequestDto dto){
        movieService.addCategoryToMovie(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> findByName(@RequestParam String name){
        return ResponseEntity.ok(movieService.findMovieByName(name));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> findAll(){
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> findMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findMovieById(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<MovieResponseDto>> findByCategory(@PathVariable Long id){
        return ResponseEntity.ok(movieService.findAllByCategory(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateMovie(@RequestBody @Valid MovieRequestDto dto,@PathVariable Long id){
        movieService.updateMovieData(id,dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
