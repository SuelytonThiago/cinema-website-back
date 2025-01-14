package com.example.project.rest.services.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password,String> {

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return true;
        }

        return password.matches("^(?=.*[A-Za-z])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");
    }
}
