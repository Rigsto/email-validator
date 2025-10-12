package io.github.rigsto.emailvalidator.result.reason;

public class ConsecutiveAt implements Reason {

    @Override
    public int code() {
        return 128;
    }

    @Override
    public String description() {
        return "@ found after another @";
    }
}
