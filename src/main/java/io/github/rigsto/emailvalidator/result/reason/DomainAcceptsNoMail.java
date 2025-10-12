package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for domains that accept no mail according to Null MX records.
 * <p>
 * This reason is generated when a domain has a Null MX record (RFC7505),
 * indicating that the domain explicitly does not accept mail. This is
 * a valid configuration for domains that should not receive email.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainAcceptsNoMail implements Reason {

    /**
     * Returns the unique code for domain accepts no mail reasons.
     * 
     * @return the reason code 154
     */
    @Override
    public int code() {
        return 154;
    }

    /**
     * Returns a description of the domain accepts no mail reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Domain accepts no mail (Null MX, RFC7505)";
    }
}
