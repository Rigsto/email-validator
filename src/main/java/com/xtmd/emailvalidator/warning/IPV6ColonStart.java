package com.xtmd.emailvalidator.warning;

public class IPV6ColonStart extends Warning {
    IPV6ColonStart() {
        super(76, ":: found at the start of the domain literal", 5322);
    }
}
