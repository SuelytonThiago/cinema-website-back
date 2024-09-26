package com.example.project.rest.dto;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Reviews;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieResponseDto {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Long id;
    private String name;
    private String description;
    private Double rating;
    private String releaseData;
    private String imageUrl;
    private String classification;
    private List<CategoryResponseDto> categories = new ArrayList<>();

    private List<ReviewsResponseDto> reviews = new ArrayList<>();

    public static MovieResponseDto of(Movies movie){
        var response = new MovieResponseDto();
        response.setName(movie.getName());
        response.setDescription(movie.getDescription());
        response.setRating(movie.getAverageRating());
        response.setReleaseData(movie.getReleaseData().format(formatter));
        response.setReviews(parseReviews(movie.getReviews()));
        response.setId(movie.getId());
        response.setImageUrl(movie.getImageUrl());
        response.setCategories(parseCategories(movie.getCategories()));
        response.setClassification(movie.getClassification());
        return response;
    }

    private static List<ReviewsResponseDto> parseReviews(List<Reviews> reviews){
        return reviews.stream().map(ReviewsResponseDto::of).collect(Collectors.toList());
    }

    private static List<CategoryResponseDto> parseCategories(Set<Categories> categories){
        return categories.stream().map(CategoryResponseDto::of).collect(Collectors.toList());
    }
}
