package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionRequestDto {

    @NotBlank(message = "this name cannot be empty or null")
    private String name;
    @NotNull(message = "this movie id cannot be null")
    private Long movieId;
    private String dateStart;
    private String dateEnd;
}
