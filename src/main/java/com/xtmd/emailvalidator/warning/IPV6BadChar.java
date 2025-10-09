package com.xtmd.emailvalidator.warning;

public class IPV6BadChar extends Warning {
    public static final int CODE = 74;

    public IPV6BadChar() {
        this.message = "Bad char in IPV6 domain literal";
        this.rfcNumber = 5322;
    }
}
