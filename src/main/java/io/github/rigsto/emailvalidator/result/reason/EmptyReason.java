package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for empty or undefined validation failures.
 * <p>
 * This reason is generated when a validation failure occurs but no specific
 * reason can be determined. This serves as a fallback reason when other
 * more specific reasons are not applicable.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class EmptyReason implements Reason {

    /**
     * Returns the unique code for empty reasons.
     * 
     * @return the reason code 0
     */
    @Override
    public int code() {
        return 0;
    }

    /**
     * Returns a description of the empty reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Empty description";
    }
}
