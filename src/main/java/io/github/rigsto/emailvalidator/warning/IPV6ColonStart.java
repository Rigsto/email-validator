package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for IPv6 addresses that start with a double colon.
 * <p>
 * This warning is generated when an IPv6 address in a domain literal
 * starts with "::" which may indicate improper formatting as per RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IPV6ColonStart extends Warning {
    /**
     * The unique warning code for IPv6 colon start warnings.
     */
    public static final int CODE = 76;

    /**
     * Creates a new IPv6 colon start warning.
     * <p>
     * Initializes the warning with a message about double colon found
     * at the start of the domain literal.
     * </p>
     */
    public IPV6ColonStart() {
        this.message = ":: found at the start of the domain literal";
        this.rfcNumber = 5322;
    }
}
