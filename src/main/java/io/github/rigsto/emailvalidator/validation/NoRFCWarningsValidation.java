package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.reason.RFCWarnings;

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
