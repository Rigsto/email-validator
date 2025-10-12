package io.github.rigsto.emailvalidator.result.reason;

public class NoDomainPart implements Reason {

    @Override
    public int code() {
        return 131;
    }

    @Override
    public String description() {
        return "No domain part found";
    }
}
