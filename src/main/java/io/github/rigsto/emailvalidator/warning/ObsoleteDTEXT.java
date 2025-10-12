package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for obsolete DTEXT characters in domain literals.
 * <p>
 * This warning is generated when obsolete DTEXT (Domain Text) characters
 * are found in a domain literal as specified in RFC 5322.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ObsoleteDTEXT extends Warning {
    /**
     * The unique warning code for obsolete DTEXT warnings.
     */
    public static final int CODE = 71;

    /**
     * Creates a new obsolete DTEXT warning.
     * <p>
     * Initializes the warning with a message about obsolete DTEXT
     * found in domain literal.
     * </p>
     */
    public ObsoleteDTEXT() {
        this.message = "Obsolete DTEXT in domain literal";
        this.rfcNumber = 5322;
    }
}
