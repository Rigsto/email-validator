package com.xtmd.emailvalidator.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum Tokens implements TokenInterface{
    OPENPARENTHESIS("OPENPARENTHESIS", "("),
    CLOSEPARENTHESIS("CLOSEPARENTHESIS", ")"),
    LOWERTHAN("LOWERTHAN", "<"),
    GREATERTHAN("GREATERTHAN", ">"),
    OPENBRACKET("OPENBRACKET", "["),
    CLOSEBRACKET("CLOSEBRACKET", "]"),
    COLON("COLON", ":"),
    SEMICOLON("SEMICOLON", ";"),
    AT("AT", "@"),
    BACKSLASH("BACKSLASH", "\\"),
    SLASH("SLASH", "/"),
    COMMA("COMMA", ","),
    DOT("DOT", "."),
    DQUOTE("DQUOTE", "\""),
    HYPHEN("HYPHEN", "-"),
    DOUBLECOLON("DOUBLECOLON", "::"),
    SP("SP", " "),
    HTAB("HTAB", "\t"),
    CR("CR", "\r"),
    LF("LF", "\n"),
    CRLF("CRLF", "\r\n"),
    IPV6TAG("IPV6TAG", "IPv6"),
    OPENQBRACKET("OPENQBRACKET", "{"),
    CLOSEQBRACKET("CLOSEQBRACKET", "}"),
    NUL("NUL", "\0")
    ;

    public static final String GENERIC = "GENERIC";
    public static final String INVALID = "INVALID";

    private static final Map<String, TokenInterface> tokensMap = new HashMap<>();
    private static final Pattern invalidUTF8 = Pattern.compile("\\p{Cc}+", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    private final String name;
    private final String text;

    Tokens(String name, String text) {
        this.name = name;
        this.text = text;
    }

    static {
        for (Tokens tokens : Tokens.values()) {
            tokensMap.put(tokens.getName(), tokens);
        }
    }

    public static TokenInterface get(String value) {
        final TokenInterface token = tokensMap.get(value);
        if (token != null) {
            return token;
        }

        if (isUTF8Invalid(value)) {
            return new Token(INVALID, value);
        }

        return new Token(GENERIC, value);
    }

    private static boolean isUTF8Invalid(String match) {
        return invalidUTF8.matcher(match).find();
    }

    public boolean equals(TokenInterface token) {
        return this.name.equals(token.getName()) && this.text.equals(token.getText());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
