package com.example.project.rest.dto;

import com.example.project.domain.entities.Movies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieResponseDto {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String name;
    private String description;
    private Double rating;
    private String releaseData;

    public static MovieResponseDto of(Movies movie){
        var response = new MovieResponseDto();
        response.setName(movie.getName());
        response.setDescription(movie.getDescription());
        response.setRating(movie.getAverageRating());
        response.setReleaseData(movie.getReleaseData().format(formatter));
        return response;
    }
}
