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

    @NotNull(message = "{category.dto.request.id}")
    private Long movieId;

    @NotBlank(message = "{category.dto.request.name}")
    private String categoryName;
}
