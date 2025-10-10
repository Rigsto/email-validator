package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.exception.EmptyValidationList;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.MultipleErrors;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleValidationWithAnd implements EmailValidation {

    public static final int STOP_ON_ERROR = 0;
    public static final int ALLOW_ALL_ERRORS = 1;

    private final Set<Warning> warnings = new HashSet<>();
    private MultipleErrors error = null;

    private final List<EmailValidation> validations;
    private final int mode;

    public MultipleValidationWithAnd(List<EmailValidation> validations, int mode) throws EmptyValidationList {
        if (validations == null || validations.isEmpty()) {
            throw new EmptyValidationList();
        }

        this.validations = validations;
        this.mode = mode;
    }

    public MultipleValidationWithAnd(List<EmailValidation> validations) throws EmptyValidationList {
        this(validations, ALLOW_ALL_ERRORS);
    }

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        boolean result = true;

        for (EmailValidation validation : validations) {
            emailLexer.reset();

            boolean validationResult = validation.isValid(email, emailLexer);
            result = result && validationResult;

            this.warnings.addAll(validation.getWarnings());

            if (!validationResult) {
                processError(validation);
            }

            if (shouldStop(result)) {
                break;
            }
        }

        return result;
    }

    private void processError(EmailValidation validation) {
        InvalidEmail ve = validation.getError();
        if (ve != null) {
            initErrorStorage();
            this.error.addReason(ve.getReason());
        }
    }

    private void initErrorStorage() {
        if (this.error == null) {
            this.error = new MultipleErrors();
        }
    }

    private boolean shouldStop(boolean currentResult) {
        return !currentResult && this.mode == STOP_ON_ERROR;
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
