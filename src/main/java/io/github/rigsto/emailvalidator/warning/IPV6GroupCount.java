package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for IPv6 addresses with invalid group counts.
 * <p>
 * This warning is generated when an IPv6 address in a domain literal has
 * a group count that is not valid according to RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IPV6GroupCount extends Warning {
    /**
     * The unique warning code for IPv6 group count warnings.
     */
    public static final int CODE = 72;

    /**
     * Creates a new IPv6 group count warning.
     * <p>
     * Initializes the warning with a message about invalid IPv6 group count.
     * </p>
     */
    public IPV6GroupCount() {
        this.message = "Group count is not IPV6 valid";
        this.rfcNumber = 5322;
    }
}
