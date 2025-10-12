package io.github.rigsto.emailvalidator.constant;

/**
 * Constants used throughout the email validation system.
 * <p>
 * This class contains various length limits and constraints used
 * for email validation according to RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class Constant {
    /**
     * Maximum allowed length for email addresses (254 characters).
     * <p>
     * This limit is specified in RFC 5321 for the total length
     * of an email address including local and domain parts.
     * </p>
     */
    public static final int EMAIL_MAX_LENGTH = 254;
    
    /**
     * Maximum allowed length for Message-ID headers (254 characters).
     * <p>
     * This limit applies to Message-ID headers in email messages.
     * </p>
     */
    public static final int EMAIL_ID_MAX_LENGTH = 254;
    
    /**
     * Maximum allowed length for the local part of an email address (64 characters).
     * <p>
     * This limit is specified in RFC 5321 for the local part
     * (the part before the @ symbol) of an email address.
     * </p>
     */
    public static final int LOCAL_PART_LENGTH = 64;
}
