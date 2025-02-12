package com.example.project.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRequestDto {

    private String comment;
    @NotNull(message = "{review.dto.request.name}")
    private Integer rating;
    private Long movieId;
}
