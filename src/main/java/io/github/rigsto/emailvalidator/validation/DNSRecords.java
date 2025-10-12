package io.github.rigsto.emailvalidator.validation;

import java.util.List;
import java.util.Map;

/**
 * Represents DNS records retrieved during email validation.
 * <p>
 * This class encapsulates the results of DNS queries performed during
 * email validation, including the actual records and any error status.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DNSRecords {

    /**
     * The DNS records retrieved from the query.
     */
    private final List<Map<String, Object>> records;
    
    /**
     * Flag indicating whether an error occurred during the DNS query.
     */
    private final boolean error;

    /**
     * Creates a new DNSRecords instance.
     * 
     * @param records the DNS records retrieved
     * @param error whether an error occurred during the query
     */
    public DNSRecords(List<Map<String, Object>> records, boolean error) {
        this.records = records;
        this.error = error;
    }

    /**
     * Returns the DNS records retrieved from the query.
     * 
     * @return the list of DNS records
     */
    public List<Map<String, Object>> getRecords() {
        return records;
    }

    /**
     * Checks if an error occurred during the DNS query.
     * 
     * @return true if an error occurred, false otherwise
     */
    public boolean withError() {
        return error;
    }
}
