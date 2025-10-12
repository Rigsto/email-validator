package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for characters that are not allowed in email addresses.
 * <p>
 * This reason is generated when an email address contains characters
 * that are not allowed according to the email address syntax rules.
 * This is a general reason for invalid character usage.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CharNotAllowed implements Reason {

    /**
     * Returns the unique code for character not allowed reasons.
     * 
     * @return the reason code 1
     */
    @Override
    public int code() {
        return 1;
    }

    /**
     * Returns a description of the character not allowed reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Character not allowed";
    }
}
