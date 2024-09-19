package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddCategoryToMovieRequestDto {

    @NotNull(message = "this movieId cannot be null")
    private Long movieId;

    @NotBlank(message = "this movie name cannot be empty or null")
    private String categoryName;
}
