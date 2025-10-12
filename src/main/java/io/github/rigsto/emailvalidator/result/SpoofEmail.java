package io.github.rigsto.emailvalidator.result;

/**
 * Represents a validation result for a potentially spoofed email address.
 * <p>
 * This class indicates that an email address has been identified as
 * potentially spoofed or using characters that could be used for
 * homograph attacks.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class SpoofEmail extends InvalidEmail {
    /**
     * Creates a new SpoofEmail result.
     */
    public SpoofEmail() {
        super(new io.github.rigsto.emailvalidator.result.reason.SpoofEmail(), "");
    }
}
