package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank(message = "{user.dto.request.name}")
    private String name;

    @NotBlank(message = "{user.dto.request.contactNumber}")
    private String contactNumber;
}
