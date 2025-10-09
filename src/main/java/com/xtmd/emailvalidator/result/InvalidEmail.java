package com.xtmd.emailvalidator.result;

import com.xtmd.emailvalidator.result.reason.Reason;

public class InvalidEmail implements Result {
    private final String token;
    public Reason reason;

    public InvalidEmail() {
        this.token = "";
        this.reason = null;
    }

    public InvalidEmail(Reason reason, String token) {
        this.token = token;
        this.reason = reason;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isInvalid() {
        return true;
    }

    @Override
    public String description() {
        return this.reason.description() + " in char " + this.token;
    }

    @Override
    public int code() {
        return this.reason.code();
    }

    public Reason getReason() {
        return this.reason;
    }
}
