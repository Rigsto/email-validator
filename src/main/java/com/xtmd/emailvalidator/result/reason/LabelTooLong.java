package com.xtmd.emailvalidator.result.reason;

public class LabelTooLong implements Reason {

    @Override
    public int code() {
        return 245;
    }

    @Override
    public String description() {
        return "Domain 'label' is longer than 63 characters";
    }
}
