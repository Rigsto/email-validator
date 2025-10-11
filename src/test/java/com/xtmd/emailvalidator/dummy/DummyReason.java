package com.xtmd.emailvalidator.dummy;

import com.xtmd.emailvalidator.result.reason.Reason;

public class DummyReason implements Reason {

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String description() {
        return "Dummy Reason";
    }
}
