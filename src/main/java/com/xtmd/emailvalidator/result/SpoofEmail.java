package com.xtmd.emailvalidator.result;

public class SpoofEmail extends InvalidEmail {
    public SpoofEmail() {
        super(new com.xtmd.emailvalidator.result.reason.SpoofEmail(), "");
    }
}
