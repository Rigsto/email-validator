package com.xtmd.emailvalidator.result.reason;

public class SpoofEmail implements Reason {

    @Override
    public int code() {
        return 298;
    }

    @Override
    public String description() {
        return "The email contains mixed UTF8 chars that makes it suspicious";
    }
}
