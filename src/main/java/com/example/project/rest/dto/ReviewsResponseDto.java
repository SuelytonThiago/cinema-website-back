package com.example.project.rest.dto;

import com.example.project.domain.entities.Reviews;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewsResponseDto {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String comment;
    private Integer rating;
    private String date;

    public static ReviewsResponseDto of(Reviews reviews){
        var response = new ReviewsResponseDto();
        response.setComment(reviews.getComment());
        response.setRating(reviews.getRating());
        response.setDate(reviews.getDate().format(formatter));
        return response;
    }
}
