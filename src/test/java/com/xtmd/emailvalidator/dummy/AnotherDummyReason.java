package com.xtmd.emailvalidator.dummy;

import com.xtmd.emailvalidator.result.reason.Reason;

public class AnotherDummyReason implements Reason {

    @Override
    public int code() {
        return 1;
    }

    @Override
    public String description() {
        return "Dummy Reason";
    }
}
