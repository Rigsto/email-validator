package io.github.rigsto.emailvalidator.result.reason;

/**
 * Interface for email validation failure reasons.
 * <p>
 * This interface defines the contract for all reason objects that explain
 * why an email validation failed. Each reason has a unique code and a
 * descriptive message that helps identify the specific validation issue.
 * </p>
 * <p>
 * Reason objects are used to provide detailed feedback about validation
 * failures, making it easier for developers to understand and handle
 * specific validation errors.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public interface Reason {
    /**
     * Returns the unique code for this reason.
     * <p>
     * Each reason type has a unique numeric code that can be used for
     * programmatic handling of specific validation failures.
     * </p>
     * 
     * @return the unique reason code
     */
    int code();
    
    /**
     * Returns a human-readable description of this reason.
     * <p>
     * The description provides a clear explanation of why the validation
     * failed, helping developers understand the specific issue.
     * </p>
     * 
     * @return the reason description
     */
    String description();
}
