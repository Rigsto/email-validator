package com.xtmd.emailvalidator.warning;

public class QuotedPart extends Warning {
    public static final int CODE = 36;

    public QuotedPart(Object prevToken, Object nextToken) {
        String prev = tokenToString(prevToken);
        String next = tokenToString(nextToken);

        this.message = "Deprecated Quoted String found between " + prev + " and " + next;
    }

    private String tokenToString(Object token) {
        if (token == null) return "null";
        if (token instanceof Enum<?>) return ((Enum<?>) token).name();
        return token.toString();
    }
}
