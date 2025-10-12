package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for missing domain part in email addresses.
 * <p>
 * This reason is generated when an email address is missing the domain part
 * (the part after the @ symbol). A valid email address must have both
 * a local part and a domain part.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class NoDomainPart implements Reason {

    /**
     * Returns the unique code for no domain part reasons.
     * 
     * @return the reason code 131
     */
    @Override
    public int code() {
        return 131;
    }

    /**
     * Returns a description of the no domain part reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "No domain part found";
    }
}
