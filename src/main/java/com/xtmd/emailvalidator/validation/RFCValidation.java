package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.EmailParser;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.reason.ExceptionFound;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.HashSet;
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
    public Set<Warning> getWarnings() {
        return this.warnings;
    }
}
