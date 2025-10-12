package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for missing closing bracket in domain literals.
 * <p>
 * This reason is generated when a domain literal is opened with a '['
 * bracket but the corresponding closing ']' bracket is not found,
 * which violates the domain literal syntax rules.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class ExpectingDomainLiteralClose implements Reason {

    /**
     * Returns the unique code for expecting domain literal close reasons.
     * 
     * @return the reason code 137
     */
    @Override
    public int code() {
        return 137;
    }

    /**
     * Returns a description of the expecting domain literal close reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "Closing bracket ']' for domain literal not found";
    }
}
