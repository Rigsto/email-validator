package com.xtmd.emailvalidator.result.reason;

public class ExceptionFound implements Reason {

    private final Exception exception;

    public ExceptionFound(Exception exception) {
        this.exception = exception;
    }

    @Override
    public int code() {
        return 999;
    }

    @Override
    public String description() {
        return this.exception.getMessage();
    }
}
