package com.xtmd.emailvalidator.result.reason;

public class UnOpenedComment implements Reason {

    @Override
    public int code() {
        return 152;
    }

    @Override
    public String description() {
        return "Missing opening comment parentheses";
    }
}
