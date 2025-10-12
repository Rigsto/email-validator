package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for exceptions encountered during email validation.
 * <p>
 * This reason is generated when an exception occurs during the email
 * validation process. It wraps the original exception and provides
 * its message as the reason description.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ExceptionFound implements Reason {

    /**
     * The exception that was encountered during validation.
     */
    private final Exception exception;

    /**
     * Creates a new exception found reason with the specified exception.
     * 
     * @param exception the exception that was encountered
     */
    public ExceptionFound(Exception exception) {
        this.exception = exception;
    }

    /**
     * Returns the unique code for exception found reasons.
     * 
     * @return the reason code 999
     */
    @Override
    public int code() {
        return 999;
    }

    /**
     * Returns the message from the encountered exception.
     * 
     * @return the exception message
     */
    @Override
    public String description() {
        return this.exception.getMessage();
    }
}
