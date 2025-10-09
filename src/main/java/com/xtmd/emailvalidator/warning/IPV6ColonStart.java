package com.xtmd.emailvalidator.warning;

public class IPV6ColonStart extends Warning {
    public static final int CODE = 76;

    public IPV6ColonStart() {
        this.message = ":: found at the start of the domain literal";
        this.rfcNumber = 5322;
    }
}
