package io.github.rigsto.emailvalidator.result.reason;

public class CRLFAtTheEnd implements Reason {

    @Override
    public int code() {
        return 149;
    }

    @Override
    public String description() {
        return "CRLF at the end";
    }
}
