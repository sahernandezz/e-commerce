package com.challenge.ecommercebackend.modules.user.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidation implements ConstraintValidator<Password, String> {

    private int length;

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.length = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean resultado = true;
        if (value != null) {
            if (value.length() < this.length) {
                resultado = false;
            } else if (!value.matches(".*[0-9].*")) {
                resultado = false;
            } else if (!value.matches(".*[!@#$%^&*()_+].*")) {
                resultado = false;
            } else if (!value.matches(".*[A-Z].*")) {
                resultado = false;
            } else if (!value.matches(".*[a-z].*")) {
                resultado = false;
            }
        }
        return resultado;
    }
}
