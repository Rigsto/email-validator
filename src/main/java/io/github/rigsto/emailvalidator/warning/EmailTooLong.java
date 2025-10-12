package io.github.rigsto.emailvalidator.warning;

import io.github.rigsto.emailvalidator.constant.Constant;

/**
 * Warning for email addresses that exceed the maximum allowed length.
 * <p>
 * This warning is generated when an email address exceeds the maximum length
 * defined in the email validation constants.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class EmailTooLong extends Warning {
    /**
     * The unique warning code for email too long warnings.
     */
    public static final int CODE = 66;

    /**
     * Creates a new email too long warning.
     * <p>
     * Initializes the warning with a message indicating the email exceeds
     * the maximum allowed length.
     * </p>
     */
    public EmailTooLong() {
        this.message = "Email too long, exceeds " + Constant.EMAIL_MAX_LENGTH;
    }

}
