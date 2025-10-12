package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for missing DNS records for email validation.
 * <p>
 * This reason is generated when no MX (Mail Exchange) or A DNS records
 * are found for the domain part of an email address, which prevents
 * proper validation of the email address.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class NoDNSRecord implements Reason {

    /**
     * Returns the unique code for no DNS record reasons.
     * 
     * @return the reason code 5
     */
    @Override
    public int code() {
        return 5;
    }

    /**
     * Returns a description of the no DNS record reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "No MX or A DNS record was found for this email";
    }
}
