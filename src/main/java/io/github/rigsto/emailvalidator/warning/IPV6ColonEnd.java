package io.github.rigsto.emailvalidator.warning;

public class IPV6ColonEnd extends Warning {
    public static final int CODE = 77;

    public IPV6ColonEnd() {
        this.message = ":: found at the end of the domain literal";
        this.rfcNumber = 5322;
    }
}
