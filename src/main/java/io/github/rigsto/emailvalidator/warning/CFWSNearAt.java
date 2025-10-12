package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for deprecated folding whitespace near the @ symbol.
 * <p>
 * This warning is generated when deprecated folding whitespace (CFWS) is found
 * near the @ symbol in an email address, which may indicate improper formatting.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CFWSNearAt extends Warning {
    /**
     * The unique warning code for CFWS near @ warnings.
     */
    public static final int CODE = 49;

    /**
     * Creates a new CFWS near @ warning.
     * <p>
     * Initializes the warning with a message about deprecated folding
     * whitespace found near the @ symbol.
     * </p>
     */
    public CFWSNearAt() {
        this.message = "Deprecated folding white space near @";
    }
}
