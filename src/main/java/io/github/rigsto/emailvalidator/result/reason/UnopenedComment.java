package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for missing opening comment parentheses.
 * <p>
 * This reason is generated when a closing comment parenthesis is found
 * without a corresponding opening comment parenthesis, which violates
 * the email address comment syntax rules.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class UnopenedComment implements Reason {

    /**
     * Returns the unique code for unopened comment reasons.
     * 
     * @return the reason code 152
     */
    @Override
    public int code() {
        return 152;
    }

    /**
     * Returns a description of the unopened comment reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Missing opening comment parentheses";
    }
}
