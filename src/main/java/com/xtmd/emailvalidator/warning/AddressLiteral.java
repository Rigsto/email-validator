package com.xtmd.emailvalidator.warning;

public class AddressLiteral extends Warning {
    public static final int CODE = 12;

    public AddressLiteral() {
        this.message = "Address literal in domain part";
        this.rfcNumber = 5321;
    }
}
