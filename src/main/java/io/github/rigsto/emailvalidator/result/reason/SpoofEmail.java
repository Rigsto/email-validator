package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for potentially spoofed email addresses.
 * <p>
 * This reason is generated when an email address contains mixed UTF8
 * characters that make it suspicious and potentially used for spoofing
 * or phishing purposes. This helps identify potentially malicious
 * email addresses.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class SpoofEmail implements Reason {

    /**
     * Returns the unique code for spoof email reasons.
     * 
     * @return the reason code 298
     */
    @Override
    public int code() {
        return 298;
    }

    /**
     * Returns a description of the spoof email reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "The email contains mixed UTF8 chars that makes it suspicious";
    }
}
