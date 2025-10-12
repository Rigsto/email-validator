package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for consecutive @ symbols in email addresses.
 * <p>
 * This reason is generated when multiple @ symbols are found consecutively
 * in an email address, which violates the basic email address format
 * that should contain exactly one @ symbol separating local and domain parts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ConsecutiveAt implements Reason {

    /**
     * Returns the unique code for consecutive @ reasons.
     * 
     * @return the reason code 128
     */
    @Override
    public int code() {
        return 128;
    }

    /**
     * Returns a description of the consecutive @ reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "@ found after another @";
    }
}
