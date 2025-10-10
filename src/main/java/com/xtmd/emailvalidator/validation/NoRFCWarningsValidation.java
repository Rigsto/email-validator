package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.reason.RFCWarnings;

public class NoRFCWarningsValidation extends RFCValidation {
    private InvalidEmail error = null;

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        if (!super.isValid(email, emailLexer)) {
            return false;
        }

        if (this.getWarnings().isEmpty()) {
            return true;
        }

        this.error = new InvalidEmail(new RFCWarnings(), "");

        return false;
    }

    @Override
    public InvalidEmail getError() {
        return this.error != null ? this.error : super.getError();
    }
}
