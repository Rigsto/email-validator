package io.github.rigsto.emailvalidator.warning;

public class NoDNSMXRecord extends Warning {
    public static final int CODE = 6;

    public NoDNSMXRecord() {
        this.message = "No MX DNS record was found for this email";
        this.rfcNumber = 5321;
    }
}
