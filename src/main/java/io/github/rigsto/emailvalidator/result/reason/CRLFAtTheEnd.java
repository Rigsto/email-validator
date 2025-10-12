package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for CRLF tokens found at the end of email addresses.
 * <p>
 * This reason is generated when CRLF (Carriage Return + Line Feed) tokens
 * are found at the end of an email address, which indicates improper
 * formatting or parsing issues.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CRLFAtTheEnd implements Reason {

    /**
     * Returns the unique code for CRLF at the end reasons.
     * 
     * @return the reason code 149
     */
    @Override
    public int code() {
        return 149;
    }

    /**
     * Returns a description of the CRLF at the end reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "CRLF at the end";
    }
}
