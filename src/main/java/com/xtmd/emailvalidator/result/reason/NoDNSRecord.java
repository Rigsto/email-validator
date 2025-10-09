package com.xtmd.emailvalidator.result.reason;

public class NoDNSRecord implements Reason {

    @Override
    public int code() {
        return 5;
    }

    @Override
    public String description() {
        return "No MX or A DNS record was found for this email";
    }
}
