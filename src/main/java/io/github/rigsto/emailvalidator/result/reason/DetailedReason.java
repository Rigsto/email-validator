package io.github.rigsto.emailvalidator.result.reason;

abstract class DetailedReason implements Reason {
    protected String detailedReason;

    public DetailedReason(String detailedReason) {
        this.detailedReason = detailedReason;
    }
}
