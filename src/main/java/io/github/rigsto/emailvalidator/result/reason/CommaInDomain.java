package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for comma characters found in domain parts.
 * <p>
 * This reason is generated when a comma (,) character is found in the
 * domain part of an email address, which is not allowed according to
 * email address syntax rules.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class CommaInDomain implements Reason {

    /**
     * Returns the unique code for comma in domain reasons.
     * 
     * @return the reason code 200
     */
    @Override
    public int code() {
        return 200;
    }

    /**
     * Returns a description of the comma in domain reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Comma ',' is not allowed in domain part";
    }
}
