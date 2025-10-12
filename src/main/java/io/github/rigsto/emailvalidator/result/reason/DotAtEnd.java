package io.github.rigsto.emailvalidator.result.reason;

public class DotAtEnd implements Reason {

    @Override
    public int code() {
        return 142;
    }

    @Override
    public String description() {
        return "Dot at the end";
    }
}
