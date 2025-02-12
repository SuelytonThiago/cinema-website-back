package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieRequestDto implements Serializable {

    @NotBlank(message = "{movie.dto.request.name}")
    private String name;

    @NotBlank(message = "{movie.dto.request.description}")
    private String description;

    @NotBlank(message = "{movie.dto.request.releaseData}")
    private String releaseData;

    @NotBlank(message = "{movie.dto.request.classification}")
    private String classification;

    @NotBlank(message = "{movie.dto.request.imageUrl}")
    private String imageUrl;

}
