package com.example.project.rest.dto;

import com.example.project.rest.services.validations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto implements Serializable {

    @NotBlank(message = "{user.dto.request.name}")
    private String name;

    @NotBlank(message = "{email.dto.request.invalid}")
    @Email(message = "{user.dto.request.email}")
    private String email;

    @NotBlank(message = "{user.dto.request.cpf}")
    @CPF(message = "{cpf.dto.request.invalid}")
    private String cpf;

    @NotBlank(message = "{user.dto.request.contactNumber}")
    private String contactNumber;

    @Password(message = "{password.dto.request.invalid}")
    private String password;

    private String profileImg;
}
