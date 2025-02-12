package com.example.project.rest.dto;

import com.example.project.rest.services.validations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginDto {

    @NotBlank(message = "{email.dto.request.invalid}")
    @Email(message = "{user.dto.request.email}")
    private String email;

    @Password(message = "{password.dto.request.invalid}")
    private String password;
}
