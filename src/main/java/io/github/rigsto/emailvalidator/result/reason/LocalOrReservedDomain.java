package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for local, mDNS, or reserved domains.
 * <p>
 * This reason is generated when an email address uses a local, mDNS
 * (multicast DNS), or reserved domain as specified in RFC2606 and RFC6762.
 * These domains are not valid for public email addresses.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class LocalOrReservedDomain implements Reason {

    /**
     * Returns the unique code for local or reserved domain reasons.
     * 
     * @return the reason code 153
     */
    @Override
    public int code() {
        return 153;
    }

    /**
     * Returns a description of the local or reserved domain reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Local, mDNS or reserved domain (RFC2606, RFC6762)";
    }
}
