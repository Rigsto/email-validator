package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for Top-Level Domain (TLD) related issues.
 * <p>
 * This warning is generated when there are issues with the top-level domain
 * part of an email address, as specified in RFC 5321.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class TLD extends Warning {
    /**
     * The unique warning code for TLD warnings.
     */
    public static final int CODE = 9;

    /**
     * Creates a new TLD warning.
     * <p>
     * Initializes the warning with a message indicating RFC 5321 TLD issues.
     * </p>
     */
    public TLD() {
        this.message = "RFC5321, TLD";
    }
}
