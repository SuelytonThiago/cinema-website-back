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

    @NotBlank(message = "the name cannot be empty or null")
    private String name;

    @NotBlank(message = "the description cannot be empty or null")
    private String description;

    @NotBlank(message = "the release data cannot be empty or null")
    private String releaseData;

}
