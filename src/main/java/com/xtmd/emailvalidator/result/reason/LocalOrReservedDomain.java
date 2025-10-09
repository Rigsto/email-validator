package com.xtmd.emailvalidator.result.reason;

public class LocalOrReservedDomain implements Reason {

    @Override
    public int code() {
        return 153;
    }

    @Override
    public String description() {
        return "Local, mDNS or reserved domain (RFC2606, RFC6762)";
    }
}
