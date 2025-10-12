package io.github.rigsto.emailvalidator.warning;

public class IPV6GroupCount extends Warning {
    public static final int CODE = 72;

    public IPV6GroupCount() {
        this.message = "Group count is not IPV6 valid";
        this.rfcNumber = 5322;
    }
}
