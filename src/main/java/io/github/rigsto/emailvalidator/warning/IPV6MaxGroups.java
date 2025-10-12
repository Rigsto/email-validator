package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for IPv6 addresses that exceed the maximum number of groups.
 * <p>
 * This warning is generated when an IPv6 address in a domain literal
 * contains more groups than allowed by RFC 5321.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IPV6MaxGroups extends Warning {
    /**
     * The unique warning code for IPv6 max groups warnings.
     */
    public static final int CODE = 75;

    /**
     * Creates a new IPv6 max groups warning.
     * <p>
     * Initializes the warning with a message about exceeding the maximum
     * number of IPv6 groups.
     * </p>
     */
    public IPV6MaxGroups() {
        this.message = "Reached the maximum number of IPV6 groups allowed";
        this.rfcNumber = 5321;
    }
}
