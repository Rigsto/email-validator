package com.xtmd.emailvalidator.result.reason;

public class ConsecutiveDot implements Reason {

    @Override
    public int code() {
        return 132;
    }

    @Override
    public String description() {
        return "Consecutive DOT found";
    }
}
