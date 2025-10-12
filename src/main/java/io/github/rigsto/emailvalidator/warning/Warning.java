package io.github.rigsto.emailvalidator.warning;

/**
 * Abstract base class for all email validation warnings.
 * <p>
 * This class provides the common structure and behavior for all warning types
 * that can be generated during email validation. Each warning has a unique
 * code, a descriptive message, and an associated RFC number when applicable.
 * </p>
 * <p>
 * Warnings represent non-fatal issues found during email validation that
 * indicate potential problems or deprecated usage patterns but do not
 * necessarily make the email invalid.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public abstract class Warning {
    /**
     * Default warning code for the base Warning class.
     * Subclasses should override this with their own unique code.
     */
    public static final int CODE = 0;

    /**
     * The descriptive message explaining the warning.
     */
    protected String message = "";
    
    /**
     * The RFC number associated with this warning, if applicable.
     * Zero indicates no specific RFC association.
     */
    protected int rfcNumber = 0;

    /**
     * Returns the descriptive message for this warning.
     * 
     * @return the warning message
     */
    public String message() {
        return message;
    }

    /**
     * Returns the unique code for this warning type.
     * <p>
     * This method uses reflection to get the CODE field from the actual
     * subclass, falling back to the default CODE if reflection fails.
     * </p>
     * 
     * @return the warning code
     */
    public int code() {
        try {
            return (int) this.getClass().getField("CODE").get(null);
        } catch (Exception e) {
            return CODE;
        }
    }

    /**
     * Returns the RFC number associated with this warning.
     * 
     * @return the RFC number, or 0 if no RFC is associated
     */
    public int rfcNumber() {
        return rfcNumber;
    }

    /**
     * Returns a string representation of this warning.
     * <p>
     * The format includes the message, RFC number, and internal code.
     * </p>
     * 
     * @return a string representation of the warning
     */
    @Override
    public String toString() {
        return message() + " rfc: " + rfcNumber + " internal code: " + code();
    }

    /**
     * Compares this warning with another object for equality.
     * <p>
     * Two warnings are considered equal if they have the same warning code.
     * </p>
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Warning warning) {
            return warning.code() == this.code();
        }
        return false;
    }

    /**
     * Returns the hash code for this warning.
     * <p>
     * The hash code is based on the warning code.
     * </p>
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return this.code();
    }
}
