package io.github.rigsto.emailvalidator.result;

/**
 * Interface for email validation results.
 * <p>
 * This interface defines the contract for all validation result objects,
 * providing methods to check validity status and retrieve descriptive
 * information about the result.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public interface Result {
    /**
     * Checks if the validation result indicates a valid email.
     * 
     * @return true if the email is valid, false otherwise
     */
    boolean isValid();
    
    /**
     * Checks if the validation result indicates an invalid email.
     * 
     * @return true if the email is invalid, false otherwise
     */
    boolean isInvalid();
    
    /**
     * Returns a human-readable description of the validation result.
     * 
     * @return a description of the result
     */
    String description();
    
    /**
     * Returns a numeric code associated with the validation result.
     * 
     * @return the result code
     */
    int code();
}
