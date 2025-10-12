package io.github.rigsto.emailvalidator.validation;

import java.util.List;
import java.util.Map;

public class DNSRecords {

    private final List<Map<String, Object>> records;
    private final boolean error;

    public DNSRecords(List<Map<String, Object>> records, boolean error) {
        this.records = records;
        this.error = error;
    }

    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public boolean withError() {
        return error;
    }
}
