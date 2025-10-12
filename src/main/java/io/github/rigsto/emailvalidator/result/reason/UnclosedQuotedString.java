package io.github.rigsto.emailvalidator.result.reason;

public class UnclosedQuotedString implements Reason {

    @Override
    public int code() {
        return 145;
    }

    @Override
    public String description() {
        return "Unclosed quoted string";
    }
}
