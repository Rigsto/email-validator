package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for double colon found after IPv6 tag in domain literals.
 * <p>
 * This warning is generated when a double colon is found after an IPv6 tag
 * in a domain literal, which may indicate improper IPv6 formatting as per RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IPV6DoubleColon extends Warning {
    /**
     * The unique warning code for IPv6 double colon warnings.
     */
    public static final int CODE = 73;

    /**
     * Creates a new IPv6 double colon warning.
     * <p>
     * Initializes the warning with a message about double colon found
     * after IPv6 tag.
     * </p>
     */
    public IPV6DoubleColon() {
        this.message = "Double colon found after IPV6 tag";
        this.rfcNumber = 5322;
    }
}
