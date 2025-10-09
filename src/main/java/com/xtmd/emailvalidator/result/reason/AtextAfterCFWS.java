package com.xtmd.emailvalidator.result.reason;

public class AtextAfterCFWS implements Reason {

    @Override
    public int code() {
        return 133;
    }

    @Override
    public String description() {
        return "ATEXT found after CFWS";
    }
}
