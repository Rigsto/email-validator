package com.xtmd.emailvalidator.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class LexerConstant {
    public static final int S_EMPTY             = -1;
    public static final int C_NUL               = 0;
    public static final int S_HTAB              = 9;
    public static final int S_LF                = 10;
    public static final int S_CR                = 13;
    public static final int S_SP                = 32;
    public static final int EXCLAMATION         = 33;
    public static final int S_DQUOTE            = 34;
    public static final int NUMBER_SIGN         = 35;
    public static final int DOLLAR              = 36;
    public static final int PERCENTAGE          = 37;
    public static final int AMPERSAND           = 38;
    public static final int S_SQUOTE            = 39;
    public static final int S_OPENPARENTHESIS   = 40;
    public static final int S_CLOSEPARENTHESIS  = 41;
    public static final int ASTERISK            = 42;
    public static final int S_PLUS              = 43;
    public static final int S_COMMA             = 44;
    public static final int S_HYPHEN            = 45;
    public static final int S_DOT               = 46;
    public static final int S_SLASH             = 47;
    public static final int S_COLON             = 58;
    public static final int S_SEMICOLON         = 59;
    public static final int S_LOWERTHAN         = 60;
    public static final int S_EQUAL             = 61;
    public static final int S_GREATERTHAN       = 62;
    public static final int QUESTIONMARK        = 63;
    public static final int S_AT                = 64;
    public static final int S_OPENBRACKET       = 91;
    public static final int S_BACKSLASH         = 92;
    public static final int S_CLOSEBRACKET      = 93;
    public static final int CARET               = 94;
    public static final int S_UNDERSCORE        = 95;
    public static final int S_BACKTICK          = 96;
    public static final int S_OPENCURLYBRACES   = 123;
    public static final int S_PIPE              = 124;
    public static final int S_CLOSECURLYBRACES  = 125;
    public static final int S_TILDE             = 126;
    public static final int C_DEL               = 127;
    public static final int INVERT_QUESTIONMARK = 168;
    public static final int INVERT_EXCLAMATION  = 173;
    public static final int GENERIC             = 300;
    public static final int S_IPV6TAG           = 301;
    public static final int INVALID             = 302;
    public static final int CRLF                = 1310;
    public static final int S_DOUBLECOLON       = 5858;
    public static final int ASCII_INVALID_FROM  = 127;
    public static final int ASCII_INVALID_TO    = 199;

    public static final Map<String, Integer> charValue = new LinkedHashMap<>(Map.ofEntries(
            Map.entry("{", S_OPENCURLYBRACES),
            Map.entry("}", S_CLOSECURLYBRACES),
            Map.entry("(", S_OPENPARENTHESIS),
            Map.entry(")", S_CLOSEPARENTHESIS),
            Map.entry("<", S_LOWERTHAN),
            Map.entry(">", S_GREATERTHAN),
            Map.entry("[", S_OPENBRACKET),
            Map.entry("]", S_CLOSEBRACKET),
            Map.entry(":", S_COLON),
            Map.entry(";", S_SEMICOLON),
            Map.entry("@", S_AT),
            Map.entry("\\", S_BACKSLASH),
            Map.entry("/", S_SLASH),
            Map.entry(",", S_COMMA),
            Map.entry(".", S_DOT),
            Map.entry("'", S_SQUOTE),
            Map.entry("`", S_BACKTICK),
            Map.entry("\"", S_DQUOTE),
            Map.entry("-", S_HYPHEN),
            Map.entry("::", S_DOUBLECOLON),
            Map.entry(" ", S_SP),
            Map.entry("\t", S_HTAB),
            Map.entry("\r", S_CR),
            Map.entry("\n", S_LF),
            Map.entry("\r\n", CRLF),
            Map.entry("IPv6", S_IPV6TAG),
            Map.entry("", S_EMPTY),
            Map.entry("\0", C_NUL),
            Map.entry("*", ASTERISK),
            Map.entry("!", EXCLAMATION),
            Map.entry("&", AMPERSAND),
            Map.entry("^", CARET),
            Map.entry("$", DOLLAR),
            Map.entry("%", PERCENTAGE),
            Map.entry("~", S_TILDE),
            Map.entry("|", S_PIPE),
            Map.entry("_", S_UNDERSCORE),
            Map.entry("=", S_EQUAL),
            Map.entry("+", S_PLUS),
            Map.entry("¿", INVERT_QUESTIONMARK),
            Map.entry("?", QUESTIONMARK),
            Map.entry("#", NUMBER_SIGN),
            Map.entry("¡", INVERT_EXCLAMATION)
    ));
}
