package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.EmailParser;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.reason.ExceptionFound;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validation strategy for RFC-compliant email address validation.
 * <p>
 * This validation performs basic RFC compliance checking using the EmailParser
 * to validate email syntax according to RFC standards. It collects warnings
 * and errors from the parsing process.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class RFCValidation implements EmailValidation {

    /**
     * Set of warnings collected during validation.
     */
    private Set<Warning> warnings = new HashSet<>();
    
    /**
     * The error from the last validation, if any.
     */
    private InvalidEmail error = null;

    /**
     * Validates an email address for RFC compliance.
     * <p>
     * Uses the EmailParser to perform syntax validation according to
     * RFC standards and collects any warnings or errors generated.
     * </p>
     * 
     * @param email the email address to validate
     * @param emailLexer the lexer to use for tokenization
     * @return true if the email is RFC-compliant, false otherwise
     */
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
     * 
     * @return a list of warnings
     */
    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }
}
