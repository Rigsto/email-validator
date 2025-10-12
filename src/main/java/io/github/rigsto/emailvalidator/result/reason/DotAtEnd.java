package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for email addresses that end with a dot character.
 * <p>
 * This reason is generated when an email address ends with a dot (.)
 * character, which violates the email address syntax rules that do not
 * allow dots at the end of local or domain parts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DotAtEnd implements Reason {

    /**
     * Returns the unique code for dot at end reasons.
     * 
     * @return the reason code 142
     */
    @Override
    public int code() {
        return 142;
    }

    /**
     * Returns a description of the dot at end reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Dot at the end";
    }
}
