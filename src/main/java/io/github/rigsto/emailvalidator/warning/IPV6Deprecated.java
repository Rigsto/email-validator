package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for deprecated IPv6 address format in email domain literals.
 * <p>
 * This warning is generated when an IPv6 address in a domain literal uses
 * a deprecated format as specified in RFC 5321.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IPV6Deprecated extends Warning {
    /**
     * The unique warning code for deprecated IPv6 warnings.
     */
    public static final int CODE = 13;

    /**
     * Creates a new deprecated IPv6 warning.
     * <p>
     * Initializes the warning with a message about deprecated IPv6 format.
     * </p>
     */
    public IPV6Deprecated() {
        this.message = "Deprecated form of IPV6";
        this.rfcNumber = 5321;
    }
}
