package io.github.rigsto.emailvalidator.warning;

public class DeprecatedComment extends Warning {
    public static final int CODE = 37;

    public DeprecatedComment() {
        this.message = "Deprecated comments";
    }
}
