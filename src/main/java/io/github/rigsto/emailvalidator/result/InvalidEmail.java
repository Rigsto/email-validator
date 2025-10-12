package io.github.rigsto.emailvalidator.result;

import io.github.rigsto.emailvalidator.result.reason.Reason;

/**
 * Represents a failed email validation result.
 * <p>
 * This class indicates that an email address has failed validation
 * and contains information about the specific reason for the failure
 * and the token that caused the issue.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class InvalidEmail implements Result {
    /**
     * The token that caused the validation failure.
     */
    private final String token;
    
    /**
     * The reason for the validation failure.
     */
    public Reason reason;

    /**
     * Creates a new InvalidEmail with no specific reason or token.
     */
    public InvalidEmail() {
        this.token = "";
        this.reason = null;
    }

    /**
     * Creates a new InvalidEmail with the specified reason and token.
     * 
     * @param reason the reason for the validation failure
     * @param token the token that caused the failure
     */
    public InvalidEmail(Reason reason, String token) {
        this.token = token;
        this.reason = reason;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isInvalid() {
        return true;
    }

    @Override
    public String description() {
        return this.reason.description() + " in char " + this.token;
    }

    @Override
    public int code() {
        return this.reason.code();
    }

    /**
     * Returns the reason for the validation failure.
     * 
     * @return the reason for the failure
     */
    public Reason getReason() {
        return this.reason;
    }
}
