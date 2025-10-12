package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for inability to retrieve DNS records for email validation.
 * <p>
 * This reason is generated when the email validator is unable to retrieve
 * DNS records for the host domain, which prevents proper validation of
 * the email address.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class UnableToGetDNSRecord extends NoDNSRecord {

    /**
     * Returns the unique code for unable to get DNS record reasons.
     * 
     * @return the reason code 3
     */
    @Override
    public int code() {
        return 3;
    }

    /**
     * Returns a description of the unable to get DNS record reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Unable to get DNS record for the host";
    }
}
