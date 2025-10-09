package com.xtmd.emailvalidator.result.reason;

public class RFCWarnings implements Reason {

    @Override
    public int code() {
        return 997;
    }

    @Override
    public String description() {
        return "Warnings found after validating";
    }
}
