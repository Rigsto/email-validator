package io.github.rigsto.emailvalidator.result.reason;

public class ExpectingCTEXT implements Reason {

    @Override
    public int code() {
        return 139;
    }

    @Override
    public String description() {
        return "Expecting CTEXT";
    }
}
