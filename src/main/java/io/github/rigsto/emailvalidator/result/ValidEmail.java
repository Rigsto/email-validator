package io.github.rigsto.emailvalidator.result;

public class ValidEmail implements Result {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public String description() {
        return "Valid email";
    }

    @Override
    public int code() {
        return 0;
    }
}
