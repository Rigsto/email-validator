package io.github.rigsto.emailvalidator.dummy;

import io.github.rigsto.emailvalidator.result.reason.Reason;

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
