package io.github.rigsto.emailvalidator.warning;

public class ObsoleteDTEXT extends Warning {
    public static final int CODE = 71;

    public ObsoleteDTEXT() {
        this.message = "Obsolete DTEXT in domain literal";
        this.rfcNumber = 5322;
    }
}
