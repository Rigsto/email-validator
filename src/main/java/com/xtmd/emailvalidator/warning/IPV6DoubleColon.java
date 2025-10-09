package com.xtmd.emailvalidator.warning;

public class IPV6DoubleColon extends Warning {
    public static final int CODE = 73;

    public IPV6DoubleColon() {
        this.message = "Double colon found after IPV6 tag";
        this.rfcNumber = 5322;
    }
}
