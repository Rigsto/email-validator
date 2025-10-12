package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.List;

public interface EmailValidation {
    boolean isValid(String email, EmailLexer emailLexer);
    InvalidEmail getError();
    List<Warning> getWarnings();
}
