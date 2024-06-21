package com.example.project.rest.dto;

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

    @NotBlank(message = "the name cannot be empty or null")
    private String name;

    @NotBlank(message = "the email cannot be empty or null")
    @Email(message = "insert a valid email")
    private String email;

    @NotBlank(message = "the cpf cannot be empty or null")
    @CPF(message = "insert a valid cpf")
    private String cpf;

    @NotBlank(message = "the contact Number cannot be empty or null")
    private String contactNumber;

    @NotBlank(message = "the password cannot be empty or null")
    private String password;


}
