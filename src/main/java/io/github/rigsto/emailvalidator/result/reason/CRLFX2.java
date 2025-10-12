package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for CRLF tokens found twice in email addresses.
 * <p>
 * This reason is generated when CRLF (Carriage Return + Line Feed) tokens
 * are found twice in an email address, which indicates improper formatting
 * or parsing issues.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CRLFX2 implements Reason {

    /**
     * Returns the unique code for CRLF found twice reasons.
     * 
     * @return the reason code 148
     */
    @Override
    public int code() {
        return 148;
    }

    /**
     * Returns a description of the CRLF found twice reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "CRLF tokens found twice";
    }
}
