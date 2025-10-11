package com.xtmd.emailvalidator.result;

import com.xtmd.emailvalidator.result.reason.EmptyReason;
import com.xtmd.emailvalidator.result.reason.Reason;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MultipleErrors extends InvalidEmail {
    private final Map<Integer, Reason> reasons = new LinkedHashMap<>();

    public MultipleErrors() {
        super(new EmptyReason(), "");
    }

    public void addReason(Reason reason) {
        if (reason == null) return;
        this.reasons.put(reason.code(), reason);
    }

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
