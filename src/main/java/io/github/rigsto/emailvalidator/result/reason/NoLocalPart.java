package io.github.rigsto.emailvalidator.result.reason;

public class NoLocalPart implements Reason {

    @Override
    public int code() {
        return 130;
    }

    @Override
    public String description() {
        return "No local part";
    }
}
