package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for bad characters in IPv6 domain literals.
 * <p>
 * This warning is generated when invalid characters are found in an IPv6
 * address within a domain literal, as specified in RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IPV6BadChar extends Warning {
    /**
     * The unique warning code for IPv6 bad character warnings.
     */
    public static final int CODE = 74;

    /**
     * Creates a new IPv6 bad character warning.
     * <p>
     * Initializes the warning with a message about bad characters
     * found in IPv6 domain literal.
     * </p>
     */
    public IPV6BadChar() {
        this.message = "Bad char in IPV6 domain literal";
        this.rfcNumber = 5322;
    }
}
