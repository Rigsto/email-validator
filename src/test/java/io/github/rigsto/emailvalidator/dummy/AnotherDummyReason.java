package io.github.rigsto.emailvalidator.dummy;

import io.github.rigsto.emailvalidator.result.reason.Reason;

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
