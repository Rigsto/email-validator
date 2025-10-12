package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for expecting DTEXT (Domain Text) characters.
 * <p>
 * This reason is generated when the email validator expects to find
 * DTEXT (Domain Text) characters but encounters something else.
 * DTEXT characters are valid characters for domain names.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ExpectingDTEXT implements Reason {

    /**
     * Returns the unique code for expecting DTEXT reasons.
     * 
     * @return the reason code 127
     */
    @Override
    public int code() {
        return 127;
    }

    /**
     * Returns a description of the expecting DTEXT reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Expecting DTEXT";
    }
}
