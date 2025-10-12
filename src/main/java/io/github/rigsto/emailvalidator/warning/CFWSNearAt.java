package io.github.rigsto.emailvalidator.warning;

public class CFWSNearAt extends Warning {
    public static final int CODE = 49;

    public CFWSNearAt() {
        this.message = "Deprecated folding white space near @";
    }
}
