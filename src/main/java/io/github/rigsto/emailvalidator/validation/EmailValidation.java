package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.List;

/**
 * Interface for email validation strategies.
 * <p>
 * This interface defines the contract for all email validation implementations.
 * Each validation strategy can perform different types of validation (RFC compliance,
 * DNS checks, spoof detection, etc.) and return validation results with warnings
 * and error information.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public interface EmailValidation {
    /**
     * Validates an email address using the specified lexer.
     * 
     * @param email the email address to validate
     * @param emailLexer the lexer to use for tokenization
     * @return true if the email is valid, false otherwise
     */
    boolean isValid(String email, EmailLexer emailLexer);
    
    /**
     * Returns the error from the last validation, if any.
     * 
     * @return the InvalidEmail error, or null if no error occurred
     */
    InvalidEmail getError();
    
    /**
     * Returns the warnings generated during the last validation.
     * 
     * @return a list of warnings
     */
    List<Warning> getWarnings();
}
