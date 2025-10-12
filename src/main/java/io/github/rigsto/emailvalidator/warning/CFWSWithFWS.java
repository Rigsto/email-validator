package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for consecutive folding whitespace issues.
 * <p>
 * This warning is generated when folding whitespace (CFWS) is followed by
 * folding whitespace (FWS), which may indicate improper email formatting.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CFWSWithFWS extends Warning {
    /**
     * The unique warning code for CFWS with FWS warnings.
     */
    public static final int CODE = 18;

    /**
     * Creates a new CFWS with FWS warning.
     * <p>
     * Initializes the warning with a message about consecutive folding whitespace.
     * </p>
     */
    public CFWSWithFWS() {
        this.message = "Folding whites space followed by folding white space";
        this.rfcNumber = 0;
    }
}
