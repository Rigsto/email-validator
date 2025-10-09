package com.xtmd.emailvalidator.warning;

public class CFWSWithFWS extends Warning {
    public static final int CODE = 18;

    public CFWSWithFWS() {
        this.message = "Folding whites space followed by folding white space";
        this.rfcNumber = 0;
    }
}
