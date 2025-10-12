package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.EmailParser;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.reason.ExceptionFound;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RFCValidation implements EmailValidation {

    private Set<Warning> warnings = new HashSet<>();
    private InvalidEmail error = null;

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        EmailParser parser = new EmailParser(emailLexer);

        try {
            Result result = parser.parse(email);
            this.warnings = parser.getWarnings();

            if (result.isInvalid()) {
                this.error = (InvalidEmail) result;
                return false;
            }

        } catch (Exception e) {
            this.error = new InvalidEmail(new ExceptionFound(e), "");
            return false;
        }

        return true;
    }

    @Override
    public InvalidEmail getError() {
        return this.error;
    }

    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }
}
