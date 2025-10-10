package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.Set;

public interface EmailValidation {
    boolean isValid(String email, EmailLexer emailLexer);
    InvalidEmail getError();
    Set<Warning> getWarnings();
}
