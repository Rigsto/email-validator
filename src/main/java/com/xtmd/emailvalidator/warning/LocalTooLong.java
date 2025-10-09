package com.xtmd.emailvalidator.warning;

public class LocalTooLong extends Warning {
    LocalTooLong() {
        super(64, "Local part too long, exceeds 64 chars (octets)", 5322);
    }
}
