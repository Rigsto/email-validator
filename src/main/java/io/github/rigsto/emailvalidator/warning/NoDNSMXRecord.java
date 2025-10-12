package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for email addresses with no DNS MX record.
 * <p>
 * This warning is generated when no MX (Mail Exchange) DNS record is found
 * for the domain part of an email address, as specified in RFC 5321.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class NoDNSMXRecord extends Warning {
    /**
     * The unique warning code for no DNS MX record warnings.
     */
    public static final int CODE = 6;

    /**
     * Creates a new no DNS MX record warning.
     * <p>
     * Initializes the warning with a message about missing MX DNS record.
     * </p>
     */
    public NoDNSMXRecord() {
        this.message = "No MX DNS record was found for this email";
        this.rfcNumber = 5321;
    }
}
