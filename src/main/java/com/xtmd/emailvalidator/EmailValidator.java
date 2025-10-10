package com.xtmd.emailvalidator;

import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.validation.EmailValidation;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

public class EmailValidator {

    private final EmailLexer lexer;
    private final List<Warning> warnings = new ArrayList<>();
    private InvalidEmail error = null;

    public EmailValidator() {
        this.lexer = new EmailLexer();
    }

    public boolean isValid(String email, EmailValidation validation) {
        boolean isValid = validation.isValid(email, this.lexer);
        this.warnings.clear();

        this.warnings.addAll(validation.getWarnings());
        this.error = validation.getError();

        return isValid;
    }

    public boolean hasWarnings() {
        return !this.warnings.isEmpty();
    }

    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }

    public InvalidEmail getError() {
        return this.error;
    }
}
