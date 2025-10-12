package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for missing local part in email addresses.
 * <p>
 * This reason is generated when an email address is missing the local part
 * (the part before the @ symbol). A valid email address must have both
 * a local part and a domain part.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class NoLocalPart implements Reason {

    /**
     * Returns the unique code for no local part reasons.
     * 
     * @return the reason code 130
     */
    @Override
    public int code() {
        return 130;
    }

    /**
     * Returns a description of the no local part reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "No local part";
    }
}
