package com.example.project.rest.controllers;

import com.example.project.rest.dto.ReviewRequestDto;
import com.example.project.rest.dto.ReviewsResponseDto;
import com.example.project.rest.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Void> addReviewToFilm(@RequestBody @Valid ReviewRequestDto dto,
                                                HttpServletRequest request){
        reviewService.addReview(dto,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateReview(@RequestBody @Valid ReviewRequestDto dto,
                                             @PathVariable Long id){
        reviewService.updateReview(dto,id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/data/{userId}/{movieId}")
    public ResponseEntity<ReviewsResponseDto> getUserReview(@PathVariable Long userId, @PathVariable Long movieId){
        return ResponseEntity.ok(reviewService.findByUserAndMovie(userId,movieId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
