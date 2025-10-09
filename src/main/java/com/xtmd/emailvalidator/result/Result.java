package com.xtmd.emailvalidator.result;

public interface Result {
    boolean isValid();
    boolean isInvalid();
    String description();
    int code();
}
