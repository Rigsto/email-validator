package com.xtmd.emailvalidator.warning;

public class IPV6ColonEnd extends Warning {
    IPV6ColonEnd() {
        super(77, ":: found at the end of the domain literal", 5322);
    }
}
