package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for expecting CTEXT (Comment Text) characters.
 * <p>
 * This reason is generated when the email validator expects to find
 * CTEXT (Comment Text) characters but encounters something else.
 * CTEXT characters are valid characters for email address comments.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ExpectingCTEXT implements Reason {

    /**
     * Returns the unique code for expecting CTEXT reasons.
     * 
     * @return the reason code 139
     */
    @Override
    public int code() {
        return 139;
    }

    /**
     * Returns a description of the expecting CTEXT reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Expecting CTEXT";
    }
}
