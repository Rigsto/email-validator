package com.xtmd.emailvalidator.warning;

public class QuotedPart extends Warning {
    QuotedPart(Enum<?> prevToken, Enum<?> nextToken) {
        String prevName = "";
        String nextName = "";

        if (prevToken != null) {
            prevName = prevToken.name();
        }

        if (nextToken != null) {
            nextName = nextToken.name();
        }

        setCode(36);
        setMessage("Deprecated Quoted String found between " + prevName + " and " + nextName);
    }
}
