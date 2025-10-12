package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for unclosed comments in email addresses.
 * <p>
 * This reason is generated when a comment is opened with an opening
 * parenthesis but no corresponding closing comment token is found,
 * which violates the email address comment syntax rules.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class UnclosedComment implements Reason {

    /**
     * Returns the unique code for unclosed comment reasons.
     * 
     * @return the reason code 146
     */
    @Override
    public int code() {
         return 146;
    }

    /**
     * Returns a description of the unclosed comment reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "No closing comments token found";
    }
}
