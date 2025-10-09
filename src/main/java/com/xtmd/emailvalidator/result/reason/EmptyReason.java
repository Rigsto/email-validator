package com.xtmd.emailvalidator.result.reason;

public class EmptyReason implements Reason {

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String description() {
        return "Empty description";
    }
}
