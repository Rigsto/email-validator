package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for consecutive dot characters in email addresses.
 * <p>
 * This reason is generated when multiple dot (.) characters are found
 * consecutively in an email address, which violates the email address
 * syntax rules that do not allow consecutive dots.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ConsecutiveDot implements Reason {

    /**
     * Returns the unique code for consecutive dot reasons.
     * 
     * @return the reason code 132
     */
    @Override
    public int code() {
        return 132;
    }

    /**
     * Returns a description of the consecutive dot reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Consecutive DOT found";
    }
}
