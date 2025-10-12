package io.github.rigsto.emailvalidator.result.reason;

/**
 * Abstract base class for reasons that provide detailed information.
 * <p>
 * This class extends the basic Reason interface to provide additional
 * detailed information about validation failures. It stores a detailed
 * reason string that can be used to provide more specific context
 * about the validation failure.
 * </p>
 * <p>
 * Subclasses can use the detailed reason to provide enhanced descriptions
 * that include specific details about what was found or expected.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
abstract class DetailedReason implements Reason {
    /**
     * The detailed reason information.
     * <p>
     * This field contains additional context about the validation failure
     * that can be used to provide more specific error messages.
     * </p>
     */
    protected String detailedReason;

    /**
     * Creates a new detailed reason with the specified detailed information.
     * 
     * @param detailedReason the detailed reason information
     */
    public DetailedReason(String detailedReason) {
        this.detailedReason = detailedReason;
    }
}
