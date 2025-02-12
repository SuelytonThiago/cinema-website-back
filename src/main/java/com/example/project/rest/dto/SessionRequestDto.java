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

    @NotBlank(message = "{session.dto.request.name}")
    private String name;
    @NotNull(message = "{session.dto.request.movieId}")
    private Long movieId;
    private String dateStart;
    private String dateEnd;
}
