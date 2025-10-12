package io.github.rigsto.emailvalidator.result;

import io.github.rigsto.emailvalidator.result.reason.EmptyReason;
import io.github.rigsto.emailvalidator.result.reason.Reason;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a validation result with multiple errors.
 * <p>
 * This class extends InvalidEmail to handle cases where multiple validation
 * strategies have failed, collecting all the reasons for failure into a
 * single result object.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class MultipleErrors extends InvalidEmail {
    /**
     * Map of error reasons indexed by their codes.
     */
    private final Map<Integer, Reason> reasons = new LinkedHashMap<>();

    /**
     * Creates a new MultipleErrors instance.
     */
    public MultipleErrors() {
        super(new EmptyReason(), "");
    }

    /**
     * Adds a reason to the collection of errors.
     * 
     * @param reason the reason to add
     */
    public void addReason(Reason reason) {
        if (reason == null) return;
        this.reasons.put(reason.code(), reason);
    }

    /**
     * Returns a list of all error reasons.
     * 
     * @return a list of all reasons
     */
    public List<Reason> getReasons() {
        return List.copyOf(this.reasons.values());
    }

    public Reason getReason() {
        return this.reasons.isEmpty()
                ? new EmptyReason()
                : this.reasons.values().iterator().next();
    }

    @Override
    public String description() {
        StringBuilder description = new StringBuilder();

        for (Reason reason : this.reasons.values()) {
            if (!description.isEmpty()) {
                description.append(System.lineSeparator());
            }
            description.append(reason.description());
        }

        return description.toString();
    }

    @Override
    public int code() {
        return 0;
    }
}
