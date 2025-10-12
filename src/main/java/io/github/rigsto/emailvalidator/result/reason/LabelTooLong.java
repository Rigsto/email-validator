package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for domain labels that exceed the maximum allowed length.
 * <p>
 * This reason is generated when a domain label (part between dots) exceeds
 * 63 characters, which violates the domain name length restrictions
 * specified in RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class LabelTooLong implements Reason {

    /**
     * Returns the unique code for label too long reasons.
     * 
     * @return the reason code 245
     */
    @Override
    public int code() {
        return 245;
    }

    /**
     * Returns a description of the label too long reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Domain 'label' is longer than 63 characters";
    }
}
