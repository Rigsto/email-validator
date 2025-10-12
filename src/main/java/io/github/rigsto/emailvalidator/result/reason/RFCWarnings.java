package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for RFC warnings found during email validation.
 * <p>
 * This reason is generated when warnings are found after validating
 * an email address according to RFC standards. These warnings indicate
 * potential issues but may not necessarily make the email invalid.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class RFCWarnings implements Reason {

    /**
     * Returns the unique code for RFC warnings reasons.
     * 
     * @return the reason code 997
     */
    @Override
    public int code() {
        return 997;
    }

    /**
     * Returns a description of the RFC warnings reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Warnings found after validating";
    }
}
