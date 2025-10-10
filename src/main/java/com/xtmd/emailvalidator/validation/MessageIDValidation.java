package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.MessageIDParser;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.reason.ExceptionFound;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageIDValidation implements EmailValidation {

    private Set<Warning> warnings = new HashSet<>();
    private InvalidEmail error = null;

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        MessageIDParser parser = new MessageIDParser(emailLexer);

        try {
            Result result = parser.parse(email);
            this.warnings = parser.getWarnings();

            if (result.isInvalid()) {
                this.error = (InvalidEmail) result;
                return false;
            }
        } catch (Exception ex) {
            this.error = new InvalidEmail(new ExceptionFound(ex), "");
        }

        return true;
    }

    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }

    @Override
    public InvalidEmail getError() {
        return this.error;
    }
}
