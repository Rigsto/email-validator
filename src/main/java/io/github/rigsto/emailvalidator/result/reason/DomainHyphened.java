package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for hyphen characters found in domain names.
 * <p>
 * This reason is generated when a hyphen (S_HYPHEN) character is found
 * in the domain part of an email address, which may indicate invalid
 * domain formatting.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainHyphened extends DetailedReason {

    /**
     * Creates a new domain hyphened reason with detailed information.
     * 
     * @param detailedReason the detailed reason information
     */
    public DomainHyphened(String detailedReason) {
        super(detailedReason);
    }

    /**
     * Returns the unique code for domain hyphened reasons.
     * 
     * @return the reason code 144
     */
    @Override
    public int code() {
        return 144;
    }

    /**
     * Returns a description of the domain hyphened reason.
     * 
     * @return the reason description
     */
    @Override
    public String description() {
        return "S_HYPHEN found in domain";
    }
}
