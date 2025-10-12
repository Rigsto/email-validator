package io.github.rigsto.emailvalidator.result.reason;

public class ExpectingDomainLiteralClose implements Reason {

    @Override
    public int code() {
        return 137;
    }

    @Override
    public String description() {
        return "Closing bracket ']' for domain literal not found";
    }
}
