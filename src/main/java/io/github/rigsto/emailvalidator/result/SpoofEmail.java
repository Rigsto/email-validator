package io.github.rigsto.emailvalidator.result;

public class SpoofEmail extends InvalidEmail {
    public SpoofEmail() {
        super(new io.github.rigsto.emailvalidator.result.reason.SpoofEmail(), "");
    }
}
