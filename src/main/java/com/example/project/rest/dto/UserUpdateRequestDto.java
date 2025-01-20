package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank(message = "the name cannot be empty or null")
    private String name;

    @NotBlank(message = "the contact Number cannot be empty or null")
    private String contactNumber;
}
