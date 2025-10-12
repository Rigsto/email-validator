package io.github.rigsto.emailvalidator.result.reason;

/**
 * Reason for unusual elements found in email addresses.
 * <p>
 * This reason is generated when unusual elements are found in an email
 * address that would render it invalid in the majority of cases.
 * The specific element that was found is included in the description.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class UnusualElements implements Reason {
    /**
     * The unusual element that was found.
     */
    private final String element;

    /**
     * Creates a new unusual elements reason with the specified element.
     * 
     * @param element the unusual element that was found
     */
    public UnusualElements(String element) {
        this.element = element;
    }

    /**
     * Returns the unique code for unusual elements reasons.
     * 
     * @return the reason code 201
     */
    @Override
    public int code() {
        return 201;
    }

    /**
     * Returns a description of the unusual elements reason with the found element.
     * 
     * @return the reason description with the unusual element
     */
    @Override
    public String description() {
        return "Unusual element found, would render invalid in majority of cases. Element found: " + this.element;
    }
}
