package com.xtmd.emailvalidator.result.reason;

public class ExpectingATEXT extends DetailedReason {

    public ExpectingATEXT(String detailedReason) {
        super(detailedReason);
    }

    @Override
    public int code() {
        return 137;
    }

    @Override
    public String description() {
        return "Expecting ATEXT (Printable US-ASCII). Extended: " + this.detailedReason;
    }
}
