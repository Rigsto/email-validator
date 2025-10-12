package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.MessageIDParser;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.reason.ExceptionFound;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validation strategy for Message-ID header validation.
 * <p>
 * This validation performs Message-ID header validation using the MessageIDParser
 * to validate Message-ID syntax according to RFC standards. It collects warnings
 * and errors from the parsing process.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class MessageIDValidation implements EmailValidation {

    /**
     * Set of warnings collected during validation.
     */
    private Set<Warning> warnings = new HashSet<>();
    
    /**
     * The error from the last validation, if any.
     */
    private InvalidEmail error = null;

    /**
     * Validates a Message-ID header for RFC compliance.
     * <p>
     * Uses the MessageIDParser to perform syntax validation according to
     * RFC standards and collects any warnings or errors generated.
     * </p>
     * 
     * @param email the Message-ID to validate
     * @param emailLexer the lexer to use for tokenization
     * @return true if the Message-ID is RFC-compliant, false otherwise
     */
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

    /**
     * Returns the warnings generated during the last validation.
     * 
     * @return a list of warnings
     */
    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
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
}
