package com.xtmd.emailvalidator.result.reason;

public class CommaInDomain implements Reason {

    @Override
    public int code() {
        return 200;
    }

    @Override
    public String description() {
        return "Comma ',' is not allowed in domain part";
    }
}
