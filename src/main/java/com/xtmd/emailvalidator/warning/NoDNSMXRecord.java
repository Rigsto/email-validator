package com.xtmd.emailvalidator.warning;

public class NoDNSMXRecord extends Warning {
    NoDNSMXRecord() {
        super(6, "No MX DNS record was found for this email", 5321);
    }
}
