package com.xtmd.emailvalidator.result.reason;

public class DomainAcceptsNoMail implements Reason {

    @Override
    public int code() {
        return 154;
    }

    @Override
    public String description() {
        return "Domain accepts no mail (Null MX, RFC7505)";
    }
}
