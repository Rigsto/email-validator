package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for email addresses that start with a dot character.
 * <p>
 * This reason is generated when an email address starts with a dot (.)
 * character, which violates the email address syntax rules that do not
 * allow dots at the beginning of local or domain parts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DotAtStart implements Reason {

    /**
     * Returns the unique code for dot at start reasons.
     * 
     * @return the reason code 141
     */
    @Override
    public int code() {
        return 141;
    }

    /**
     * Returns a description of the dot at start reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Starts with a DOT";
    }
}
