package io.github.rigsto.emailvalidator.validation.extra;

import com.ibm.icu.text.SpoofChecker;
import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.SpoofEmail;
import io.github.rigsto.emailvalidator.validation.EmailValidation;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

public class SpoofCheckValidation implements EmailValidation {
    private InvalidEmail error;

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        SpoofChecker.Builder builder = new SpoofChecker.Builder();
        SpoofChecker spoofChecker = builder.build();

        if (spoofChecker.failsChecks(email)) {
            this.error = new SpoofEmail();
        }

        return this.error == null;
    }

    @Override
    public InvalidEmail getError() {
        return this.error;
    }

    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>();
    }
}
