package com.xtmd.emailvalidator.validation.extra;

import com.ibm.icu.text.SpoofChecker;
import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.SpoofEmail;
import com.xtmd.emailvalidator.validation.EmailValidation;
import com.xtmd.emailvalidator.warning.Warning;

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
