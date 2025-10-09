package com.xtmd.emailvalidator.result.reason;

public class DotAtStart implements Reason {

    @Override
    public int code() {
        return 141;
    }

    @Override
    public String description() {
        return "Starts with a DOT";
    }
}
