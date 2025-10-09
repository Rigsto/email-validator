package com.xtmd.emailvalidator.warning;

public class QuotedString extends Warning {
    QuotedString(String prevToken, String nextToken) {
        super(11, "Quoted String found between " + prevToken + " and " + nextToken, 0);
    }
}
