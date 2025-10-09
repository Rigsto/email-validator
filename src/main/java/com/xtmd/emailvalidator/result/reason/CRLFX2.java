package com.xtmd.emailvalidator.result.reason;

public class CRLFX2 implements Reason {

    @Override
    public int code() {
        return 148;
    }

    @Override
    public String description() {
        return "CRLF tokens found twice";
    }
}
