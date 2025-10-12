package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for deprecated comments in email addresses.
 * <p>
 * This warning is generated when deprecated comment syntax is found
 * in an email address, which may indicate outdated formatting patterns.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DeprecatedComment extends Warning {
    /**
     * The unique warning code for deprecated comment warnings.
     */
    public static final int CODE = 37;

    /**
     * Creates a new deprecated comment warning.
     * <p>
     * Initializes the warning with a message about deprecated comments found.
     * </p>
     */
    public DeprecatedComment() {
        this.message = "Deprecated comments";
    }
}
