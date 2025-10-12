package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for domain literals in email addresses.
 * <p>
 * This warning is generated when a domain literal is found in the domain part
 * of an email address, as specified in RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainLiteral extends Warning {
    /**
     * The unique warning code for domain literal warnings.
     */
    public static final int CODE = 70;

    /**
     * Creates a new domain literal warning.
     * <p>
     * Initializes the warning with a message about domain literal found.
     * </p>
     */
    public DomainLiteral() {
        this.message = "Domain Literal";
        this.rfcNumber = 5322;
    }
}
