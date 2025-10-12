package io.github.rigsto.emailvalidator.result;

public interface Result {
    boolean isValid();
    boolean isInvalid();
    String description();
    int code();
}
