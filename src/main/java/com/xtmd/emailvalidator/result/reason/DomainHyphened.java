package com.xtmd.emailvalidator.result.reason;

public class DomainHyphened implements Reason {

    @Override
    public int code() {
        return 144;
    }

    @Override
    public String description() {
        return "S_HYPHEN found in domain";
    }
}
