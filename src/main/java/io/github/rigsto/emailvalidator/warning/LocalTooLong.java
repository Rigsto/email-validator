package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for local parts that exceed the maximum allowed length.
 * <p>
 * This warning is generated when the local part (before @) of an email address
 * exceeds 64 characters (octets) as specified in RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class LocalTooLong extends Warning {
    /**
     * The unique warning code for local too long warnings.
     */
    public static final int CODE = 64;

    /**
     * Creates a new local too long warning.
     * <p>
     * Initializes the warning with a message indicating the local part
     * exceeds 64 characters (octets).
     * </p>
     */
    public LocalTooLong() {
        this.message = "Local part too long, exceeds 64 chars (octets)";
        this.rfcNumber = 5322;
    }
}
