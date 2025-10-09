package com.xtmd.emailvalidator.result.reason;

public class CharNotAllowed implements Reason {

    @Override
    public int code() {
        return 1;
    }

    @Override
    public String description() {
        return "Character not allowed";
    }
}
