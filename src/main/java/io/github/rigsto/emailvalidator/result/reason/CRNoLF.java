package io.github.rigsto.emailvalidator.result.reason;

public class CRNoLF implements Reason {

    @Override
    public int code() {
        return 150;
    }

    @Override
    public String description() {
        return "Missing LF after CR";
    }
}
