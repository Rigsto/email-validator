package io.github.rigsto.emailvalidator.warning;

public class IPV6Deprecated extends Warning {
    public static final int CODE = 13;

    public IPV6Deprecated() {
        this.message = "Deprecated form of IPV6";
        this.rfcNumber = 5321;
    }
}
