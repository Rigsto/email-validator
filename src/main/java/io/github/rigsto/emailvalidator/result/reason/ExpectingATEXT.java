package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for expecting ATEXT (Printable US-ASCII) characters.
 * <p>
 * This reason is generated when the email validator expects to find
 * ATEXT (Printable US-ASCII) characters but encounters something else.
 * The detailed reason provides additional context about what was found
 * instead of the expected ATEXT characters.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ExpectingATEXT extends DetailedReason {

    /**
     * Creates a new expecting ATEXT reason with detailed information.
     * 
     * @param detailedReason the detailed reason information about what was found
     */
    public ExpectingATEXT(String detailedReason) {
        super(detailedReason);
    }

    /**
     * Returns the unique code for expecting ATEXT reasons.
     * 
     * @return the reason code 137
     */
    @Override
    public int code() {
        return 137;
    }

    /**
     * Returns a description of the expecting ATEXT reason with extended details.
     * 
     * @return the reason description with detailed information
     */
    @Override
    public String description() {
        return "Expecting ATEXT (Printable US-ASCII). Extended: " + this.detailedReason;
    }
}
