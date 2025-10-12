package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for address literals in email domain parts.
 * <p>
 * This warning is generated when an address literal is found in the domain part
 * of an email address, as specified in RFC 5321.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class AddressLiteral extends Warning {
    /**
     * The unique warning code for address literal warnings.
     */
    public static final int CODE = 12;

    /**
     * Creates a new address literal warning.
     * <p>
     * Initializes the warning with a message about address literal
     * found in domain part.
     * </p>
     */
    public AddressLiteral() {
        this.message = "Address literal in domain part";
        this.rfcNumber = 5321;
    }
}
