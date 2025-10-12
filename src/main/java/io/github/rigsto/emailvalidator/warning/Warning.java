package io.github.rigsto.emailvalidator.warning;

public abstract class Warning {
    public static final int CODE = 0;

    protected String message = "";
    protected int rfcNumber = 0;

    public String message() {
        return message;
    }

    public int code() {
        try {
            return (int) this.getClass().getField("CODE").get(null);
        } catch (Exception e) {
            return CODE;
        }
    }

    public int rfcNumber() {
        return rfcNumber;
    }

    @Override
    public String toString() {
        return message() + " rfc: " + rfcNumber + " internal code: " + code();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Warning warning) {
            return warning.code() == this.code();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.code();
    }
}
