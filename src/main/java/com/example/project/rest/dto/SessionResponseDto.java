package com.example.project.rest.dto;

import com.example.project.domain.entities.Reviews;
import com.example.project.domain.entities.Sessions;
import com.example.project.rest.services.exceptions.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionResponseDto{

    private Long id;
    private String movieName;
    private String sessionName;
    private String duration;
    private String movieSynopsis;
    private Double rating;
    private String imageUrl;
    private LocalDateTime dateStart;
    private List<ReviewsResponseDto> reviews = new ArrayList<>();

    public static SessionResponseDto of(Sessions session){
        var response = new SessionResponseDto();
        response.setMovieName(session.getMovie().getName());
        response.setMovieSynopsis(session.getMovie().getDescription());
        response.setDuration(obtainSessionDuration(session));
        response.setReviews(convertReviewsToResponse(session.getMovie().getReviews()));
        response.setId(session.getId());
        response.setRating(session.getMovie().getAverageRating());
        response.setImageUrl(session.getMovie().getImageUrl());
        response.setDateStart(session.getDateStart());
        response.setSessionName(session.getName());
        return response;
    }

    private static String obtainSessionDuration(Sessions sessions){
        if (sessions.getDateStart() == null || sessions.getDateEnd() == null) {
            throw new CustomException("Something went wrong");
        }
        Duration durationTime = Duration.between(sessions.getDateStart(), sessions.getDateEnd());
        long totalSeconds = durationTime.getSeconds();
        long hours = totalSeconds /3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }

    private static List<ReviewsResponseDto> convertReviewsToResponse(List<Reviews> reviews){
        return reviews.stream().map(ReviewsResponseDto::of).collect(Collectors.toList());
    }
}
