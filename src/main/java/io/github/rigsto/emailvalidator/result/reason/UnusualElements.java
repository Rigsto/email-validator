package io.github.rigsto.emailvalidator.result.reason;

public class UnusualElements implements Reason {
    private final String element;

    public UnusualElements(String element) {
        this.element = element;
    }

    @Override
    public int code() {
        return 201;
    }

    @Override
    public String description() {
        return "Unusual element found, would render invalid in majority of cases. Element found: " + this.element;
    }
}
