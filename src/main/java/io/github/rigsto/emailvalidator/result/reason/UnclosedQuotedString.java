package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for unclosed quoted strings in email addresses.
 * <p>
 * This reason is generated when a quoted string is found in an email address
 * but is not properly closed with a closing quote character. This violates
 * the email address syntax rules.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class UnclosedQuotedString implements Reason {

    /**
     * Returns the unique code for unclosed quoted string reasons.
     * 
     * @return the reason code 145
     */
    @Override
    public int code() {
        return 145;
    }

    /**
     * Returns a description of the unclosed quoted string reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Unclosed quoted string";
    }
}
