package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for domains that exceed the maximum allowed length.
 * <p>
 * This reason is generated when the domain part of an email address
 * exceeds 253 characters, which violates the email address length
 * restrictions specified in RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainTooLong implements Reason {

    /**
     * Returns the unique code for domain too long reasons.
     * 
     * @return the reason code 244
     */
    @Override
    public int code() {
        return 244;
    }

    /**
     * Returns a description of the domain too long reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Domain is longer than 253 characters";
    }
}
