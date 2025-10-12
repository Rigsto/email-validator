package io.github.rigsto.emailvalidator.warning;

public class TLD extends Warning {
    public static final int CODE = 9;

    public TLD() {
        this.message = "RFC5321, TLD";
    }
}
