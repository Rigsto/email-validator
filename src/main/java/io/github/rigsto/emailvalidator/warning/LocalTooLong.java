package io.github.rigsto.emailvalidator.warning;

public class LocalTooLong extends Warning {
    public static final int CODE = 64;

    public LocalTooLong() {
        this.message = "Local part too long, exceeds 64 chars (octets)";
        this.rfcNumber = 5322;
    }
}
