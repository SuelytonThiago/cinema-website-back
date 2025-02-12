package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryRequestDto {

    @NotBlank(message = "{category.dto.request.name}")
    private String name;
}
