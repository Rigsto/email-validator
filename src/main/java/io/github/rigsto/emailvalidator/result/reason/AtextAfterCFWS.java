package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for ATEXT characters found after CFWS (Comments and Folding Whitespace).
 * <p>
 * This reason is generated when ATEXT (Printable US-ASCII) characters are found
 * after CFWS (Comments and Folding Whitespace), which may indicate improper
 * email address formatting according to RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class AtextAfterCFWS implements Reason {

    /**
     * Returns the unique code for ATEXT after CFWS reasons.
     * 
     * @return the reason code 133
     */
    @Override
    public int code() {
        return 133;
    }

    /**
     * Returns a description of the ATEXT after CFWS reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "ATEXT found after CFWS";
    }
}
