package io.github.rigsto.emailvalidator.warning;

public class IPV6MaxGroups extends Warning {
    public static final int CODE = 75;

    public IPV6MaxGroups() {
        this.message = "Reached the maximum number of IPV6 groups allowed";
        this.rfcNumber = 5321;
    }
}
