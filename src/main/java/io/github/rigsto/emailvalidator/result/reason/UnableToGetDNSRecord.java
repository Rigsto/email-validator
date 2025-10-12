package io.github.rigsto.emailvalidator.result.reason;

public class UnableToGetDNSRecord extends NoDNSRecord {

    @Override
    public int code() {
        return 3;
    }

    @Override
    public String description() {
        return "Unable to get DNS record for the host";
    }
}
