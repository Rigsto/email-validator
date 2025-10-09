package com.xtmd.emailvalidator.warning;

public class QuotedString extends Warning {
    public static final int CODE = 11;

    public QuotedString(Object prevToken, Object nextToken) {
        String prev = tokenToString(prevToken);
        String next = tokenToString(nextToken);

        this.message = "Quoted String found between " + prev + " and " + next;
    }

    private String tokenToString(Object token) {
        if (token == null) return "null";
        if (token instanceof Enum<?>) return ((Enum<?>) token).name();
        return token.toString();
    }
}
