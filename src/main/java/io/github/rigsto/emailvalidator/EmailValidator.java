package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.validation.EmailValidation;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

/**
 * Main email validation class that provides a high-level interface for validating email addresses.
 * <p>
 * This class serves as the primary entry point for email validation functionality. It manages
 * the validation process using a lexer and various validation strategies, collecting warnings
 * and errors during the validation process.
 * </p>
 * <p>
 * The EmailValidator maintains state between validations, including warnings and errors,
 * allowing clients to retrieve detailed information about validation results.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class EmailValidator {

    /**
     * The email lexer used for tokenizing email addresses.
     */
    private final EmailLexer lexer;
    
    /**
     * List of warnings generated during validation.
     */
    private final List<Warning> warnings = new ArrayList<>();
    
    /**
     * The error result from the last validation, if any.
     */
    private InvalidEmail error = null;

    /**
     * Creates a new EmailValidator instance.
     * <p>
     * Initializes the validator with a new EmailLexer instance.
     * </p>
     */
    public EmailValidator() {
        this.lexer = new EmailLexer();
    }

    /**
     * Validates an email address using the specified validation strategy.
     * <p>
     * This method performs email validation using the provided EmailValidation
     * strategy. It clears previous warnings and errors, then collects new ones
     * from the validation process.
     * </p>
     * 
     * @param email the email address to validate
     * @param validation the validation strategy to use
     * @return true if the email is valid, false otherwise
     */
    public boolean isValid(String email, EmailValidation validation) {
        boolean isValid = validation.isValid(email, this.lexer);
        this.warnings.clear();

        this.warnings.addAll(validation.getWarnings());
        this.error = validation.getError();

        return isValid;
    }

    /**
     * Checks if the last validation generated any warnings.
     * 
     * @return true if warnings were generated, false otherwise
     */
    public boolean hasWarnings() {
        return !this.warnings.isEmpty();
    }

    /**
     * Returns a copy of the warnings generated during the last validation.
     * 
     * @return a list of warnings (defensive copy)
     */
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }

    /**
     * Returns the error from the last validation, if any.
     * 
     * @return the InvalidEmail error, or null if no error occurred
     */
    public InvalidEmail getError() {
        return this.error;
    }
}
