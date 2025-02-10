package com.example.project.rest.controllers;

import com.example.project.rest.dto.ReviewRequestDto;
import com.example.project.rest.dto.ReviewsResponseDto;
import com.example.project.rest.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/add")
    @MessageMapping("/newComment")
    @Operation(summary = "add a note and comment to the film")
    public ResponseEntity<Void> addReviewToFilm(@RequestBody @Valid ReviewRequestDto dto,
                                                HttpServletRequest request){
        var response = reviewService.addReview(dto,request);
        messagingTemplate.convertAndSend("/topic/comments", response);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "update the note and/or comment")
    public ResponseEntity<Void> updateReview(@RequestBody @Valid ReviewRequestDto dto,
                                             @PathVariable Long id){

        var response = reviewService.updateReview(dto,id);
        messagingTemplate.convertAndSend("/topic/comments", response);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/data/{userId}/{movieId}")
    @Operation(summary = "search user review by user id and movie id")
    public ResponseEntity<ReviewsResponseDto> getUserReview(@PathVariable Long userId, @PathVariable Long movieId){
        return ResponseEntity.ok(reviewService.findByUserAndMovie(userId,movieId));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "delete user review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
