package io.github.rigsto.emailvalidator.result;

/**
 * Represents a successful email validation result.
 * <p>
 * This class indicates that an email address has passed all validation
 * checks and is considered valid according to the applied validation rules.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ValidEmail implements Result {

    /**
     * Returns true indicating the email is valid.
     * 
     * @return true
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Returns false indicating the email is not invalid.
     * 
     * @return false
     */
    @Override
    public boolean isInvalid() {
        return false;
    }

    /**
     * Returns a description indicating the email is valid.
     * 
     * @return "Valid email"
     */
    @Override
    public String description() {
        return "Valid email";
    }

    /**
     * Returns the success code (0) for valid emails.
     * 
     * @return 0
     */
    @Override
    public int code() {
        return 0;
    }
}
