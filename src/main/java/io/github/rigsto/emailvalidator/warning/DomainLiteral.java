package io.github.rigsto.emailvalidator.warning;

public class DomainLiteral extends Warning {
    public static final int CODE = 70;

    public DomainLiteral() {
        this.message = "Domain Literal";
        this.rfcNumber = 5322;
    }
}
