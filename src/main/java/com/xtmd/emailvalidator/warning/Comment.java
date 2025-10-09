package com.xtmd.emailvalidator.warning;

public class Comment extends Warning {
    public static final int CODE = 12;

    public Comment() {
        this.message = "Address literal in domain part";
    }
}
