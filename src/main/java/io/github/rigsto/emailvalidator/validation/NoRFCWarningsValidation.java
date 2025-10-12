package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.reason.RFCWarnings;

/**
 * Validation strategy that extends RFC validation to reject emails with RFC warnings.
 * <p>
 * This validation performs standard RFC validation but treats any RFC warnings
 * as validation failures. It's useful when strict compliance is required
 * without any warnings.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class NoRFCWarningsValidation extends RFCValidation {
    /**
     * The error from the last validation, if any.
     */
    private InvalidEmail error = null;

    /**
     * Validates an email address for RFC compliance without warnings.
     * <p>
     * First performs standard RFC validation, then checks if any warnings
     * were generated. If warnings are present, the validation fails.
     * </p>
     * 
     * @param email the email address to validate
     * @param emailLexer the lexer to use for tokenization
     * @return true if the email is RFC-compliant with no warnings, false otherwise
     */
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

    /**
     * Returns the error from the last validation, if any.
     * <p>
     * Returns the specific error from this validation if present,
     * otherwise returns the error from the parent RFC validation.
     * </p>
     * 
     * @return the InvalidEmail error, or null if no error occurred
     */
    @Override
    public InvalidEmail getError() {
        return this.error != null ? this.error : super.getError();
    }
}
