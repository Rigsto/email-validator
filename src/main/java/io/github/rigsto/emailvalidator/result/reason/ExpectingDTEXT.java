package io.github.rigsto.emailvalidator.result.reason;

public class ExpectingDTEXT implements Reason {

    @Override
    public int code() {
        return 127;
    }

    @Override
    public String description() {
        return "Expecting DTEXT";
    }
}
