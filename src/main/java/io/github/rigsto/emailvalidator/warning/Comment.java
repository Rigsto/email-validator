package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for comments found in email addresses.
 * <p>
 * This warning is generated when comments are found in an email address,
 * which may indicate unusual formatting patterns.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class Comment extends Warning {
    /**
     * The unique warning code for comment warnings.
     */
    public static final int CODE = 12;

    /**
     * Creates a new comment warning.
     * <p>
     * Initializes the warning with a message about address literal
     * found in domain part.
     * </p>
     */
    public Comment() {
        this.message = "Address literal in domain part";
    }
}
