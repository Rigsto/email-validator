package io.github.rigsto.emailvalidator.result.reason;

public class UnclosedComment implements Reason {

    @Override
    public int code() {
         return 146;
    }

    @Override
    public String description() {
        return "No closing comments token found";
    }
}
