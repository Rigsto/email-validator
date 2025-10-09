package com.xtmd.emailvalidator.result.reason;

public class DomainHyphened extends DetailedReason {

    public DomainHyphened(String detailedReason) {
        super(detailedReason);
    }

    @Override
    public int code() {
        return 144;
    }

    @Override
    public String description() {
        return "S_HYPHEN found in domain";
    }
}
