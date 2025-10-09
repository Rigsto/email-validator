package com.xtmd.emailvalidator.result.reason;

public class DomainTooLong implements Reason {

    @Override
    public int code() {
        return 244;
    }

    @Override
    public String description() {
        return "Domain is longer than 253 characters";
    }
}
