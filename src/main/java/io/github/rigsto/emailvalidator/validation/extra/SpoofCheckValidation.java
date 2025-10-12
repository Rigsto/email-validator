package io.github.rigsto.emailvalidator.validation.extra;

import com.ibm.icu.text.SpoofChecker;
import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.SpoofEmail;
import io.github.rigsto.emailvalidator.validation.EmailValidation;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation strategy for detecting potentially spoofed email addresses.
 * <p>
 * This validation uses ICU's SpoofChecker to detect email addresses that
 * may be using characters that look similar to ASCII characters but are
 * actually different Unicode characters (homograph attacks).
 * </p>
 * <p>
 * This is an extra validation that goes beyond basic RFC compliance
 * to provide additional security against spoofing attacks.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class SpoofCheckValidation implements EmailValidation {
    /**
     * The error from the last validation, if any.
     */
    private InvalidEmail error;

    /**
     * Validates an email address for potential spoofing.
     * <p>
     * Uses ICU's SpoofChecker to detect characters that may be used
     * for homograph attacks or other spoofing techniques.
     * </p>
     * 
     * @param email the email address to validate
     * @param emailLexer the lexer (unused in this implementation)
     * @return true if the email passes spoof checks, false otherwise
     */
    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        SpoofChecker.Builder builder = new SpoofChecker.Builder();
        SpoofChecker spoofChecker = builder.build();

        if (spoofChecker.failsChecks(email)) {
            this.error = new SpoofEmail();
        }

        return this.error == null;
    }

    /**
     * Returns the error from the last validation, if any.
     * 
     * @return the InvalidEmail error, or null if no error occurred
     */
    @Override
    public InvalidEmail getError() {
        return this.error;
    }

    /**
     * Returns the warnings generated during the last validation.
     * <p>
     * Spoof check validation doesn't generate warnings.
     * </p>
     * 
     * @return an empty list of warnings
     */
    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>();
    }
}
