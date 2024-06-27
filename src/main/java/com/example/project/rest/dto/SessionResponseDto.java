package com.example.project.rest.dto;

import com.example.project.domain.entities.Reviews;
import com.example.project.domain.entities.Sessions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionResponseDto{

    private String movieName;
    private String duration;
    private String movieSynopsis;
    private List<ReviewsResponseDto> reviews = new ArrayList<>();

    public static SessionResponseDto of(Sessions session){
        var response = new SessionResponseDto();
        response.setMovieName(session.getMovie().getName());
        response.setMovieSynopsis(session.getMovie().getDescription());
        response.setDuration(obtainSessionDuration(session));
        response.setReviews(convertReviewsToResponse(session.getMovie().getReviews()));
        return response;
    }

    private static String obtainSessionDuration(Sessions sessions){
        Duration durationTime = Duration.between(sessions.getDateStart(), sessions.getDateEnd());
        long totalSeconds = durationTime.getSeconds();
        long hours = totalSeconds /3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d hours %02d minutes %02d seconds",hours,minutes,seconds);
    }

    private static List<ReviewsResponseDto> convertReviewsToResponse(List<Reviews> reviews){
        return reviews.stream().map(ReviewsResponseDto::of).collect(Collectors.toList());
    }
}
