package com.example.project.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginDto {

    @NotBlank(message = "this email cannot be empty or null")
    @Email(message = "insert a valid email")
    private String email;

    @NotBlank(message = "this password cannot be empty or null")
    private String password;
}
