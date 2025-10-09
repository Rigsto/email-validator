package com.xtmd.emailvalidator.result;

import com.xtmd.emailvalidator.result.reason.EmptyReason;
import com.xtmd.emailvalidator.result.reason.Reason;

import java.util.HashMap;
import java.util.Map;

public class MultipleErrors extends InvalidEmail {
    private final Map<Integer, Reason> reasons = new HashMap<>();
    private Reason currentReason = null;

    public MultipleErrors() {
    }

    public void addReason(Reason reason) {
        this.reasons.putIfAbsent(reason.code(), reason);
        this.currentReason = reason;
    }

    public Map<Integer, Reason> getReasons() {
        return this.reasons;
    }

    public Reason getReason() {
        return !this.reasons.isEmpty()
                ? this.currentReason
                : new EmptyReason();
    }

    @Override
    public String description() {
        StringBuilder description = new StringBuilder();

        for (Reason reason : this.reasons.values()) {
            description.append(reason.description()).append("\n");
        }

        return description.toString();
    }
}
