package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for missing Line Feed after Carriage Return.
 * <p>
 * This reason is generated when a Carriage Return (CR) character is found
 * without a following Line Feed (LF) character, which indicates improper
 * line ending formatting in email addresses.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CRNoLF implements Reason {

    /**
     * Returns the unique code for CR no LF reasons.
     * 
     * @return the reason code 150
     */
    @Override
    public int code() {
        return 150;
    }

    /**
     * Returns a description of the CR no LF reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Missing LF after CR";
    }
}
