package io.github.rigsto.emailvalidator.constant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Constants used by the email lexer for token identification.
 * <p>
 * This class contains ASCII character codes and token type constants
 * used by the EmailLexer for tokenizing email addresses. It includes
 * special characters, control characters, and custom token types
 * specific to email parsing.
 * </p>
 * <p>
 * The class also provides a mapping from character strings to their
 * corresponding token type codes for efficient lexer operations.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class LexerConstant {
    // Special token types
    /** Empty token type */
    public static final int S_EMPTY             = -1;
    /** Null character token type */
    public static final int C_NUL               = 0;
    
    // ASCII control characters
    /** Horizontal tab character (ASCII 9) */
    public static final int S_HTAB              = 9;
    /** Line feed character (ASCII 10) */
    public static final int S_LF                = 10;
    /** Carriage return character (ASCII 13) */
    public static final int S_CR                = 13;
    /** Space character (ASCII 32) */
    public static final int S_SP                = 32;
    /** Delete character (ASCII 127) */
    public static final int C_DEL               = 127;
    
    // Special characters
    /** Exclamation mark (ASCII 33) */
    public static final int EXCLAMATION         = 33;
    /** Double quote (ASCII 34) */
    public static final int S_DQUOTE            = 34;
    /** Number sign/hash (ASCII 35) */
    public static final int NUMBER_SIGN         = 35;
    /** Dollar sign (ASCII 36) */
    public static final int DOLLAR              = 36;
    /** Percentage sign (ASCII 37) */
    public static final int PERCENTAGE          = 37;
    /** Ampersand (ASCII 38) */
    public static final int AMPERSAND           = 38;
    /** Single quote (ASCII 39) */
    public static final int S_SQUOTE            = 39;
    /** Open parenthesis (ASCII 40) */
    public static final int S_OPENPARENTHESIS   = 40;
    /** Close parenthesis (ASCII 41) */
    public static final int S_CLOSEPARENTHESIS  = 41;
    /** Asterisk (ASCII 42) */
    public static final int ASTERISK            = 42;
    /** Plus sign (ASCII 43) */
    public static final int S_PLUS              = 43;
    /** Comma (ASCII 44) */
    public static final int S_COMMA             = 44;
    /** Hyphen (ASCII 45) */
    public static final int S_HYPHEN            = 45;
    /** Dot (ASCII 46) */
    public static final int S_DOT               = 46;
    /** Forward slash (ASCII 47) */
    public static final int S_SLASH             = 47;
    /** Colon (ASCII 58) */
    public static final int S_COLON             = 58;
    /** Semicolon (ASCII 59) */
    public static final int S_SEMICOLON         = 59;
    /** Less than (ASCII 60) */
    public static final int S_LOWERTHAN         = 60;
    /** Equals sign (ASCII 61) */
    public static final int S_EQUAL             = 61;
    /** Greater than (ASCII 62) */
    public static final int S_GREATERTHAN       = 62;
    /** Question mark (ASCII 63) */
    public static final int QUESTIONMARK        = 63;
    /** At symbol (ASCII 64) */
    public static final int S_AT                = 64;
    /** Open bracket (ASCII 91) */
    public static final int S_OPENBRACKET       = 91;
    /** Backslash (ASCII 92) */
    public static final int S_BACKSLASH         = 92;
    /** Close bracket (ASCII 93) */
    public static final int S_CLOSEBRACKET      = 93;
    /** Caret (ASCII 94) */
    public static final int CARET               = 94;
    /** Underscore (ASCII 95) */
    public static final int S_UNDERSCORE        = 95;
    /** Backtick (ASCII 96) */
    public static final int S_BACKTICK          = 96;
    /** Open curly brace (ASCII 123) */
    public static final int S_OPENCURLYBRACES   = 123;
    /** Pipe (ASCII 124) */
    public static final int S_PIPE              = 124;
    /** Close curly brace (ASCII 125) */
    public static final int S_CLOSECURLYBRACES  = 125;
    /** Tilde (ASCII 126) */
    public static final int S_TILDE             = 126;
    
    // Extended characters
    /** Inverted question mark (ASCII 168) */
    public static final int INVERT_QUESTIONMARK = 168;
    /** Inverted exclamation mark (ASCII 173) */
    public static final int INVERT_EXCLAMATION  = 173;
    
    // Custom token types
    /** Generic token type for unrecognized characters */
    public static final int GENERIC             = 300;
    /** IPv6 tag token type */
    public static final int S_IPV6TAG           = 301;
    /** Invalid token type */
    public static final int INVALID             = 302;
    /** CRLF sequence token type */
    public static final int CRLF                = 1310;
    /** Double colon token type */
    public static final int S_DOUBLECOLON       = 5858;
    
    // ASCII range constants
    /** Start of invalid ASCII range */
    public static final int ASCII_INVALID_FROM  = 127;
    /** End of invalid ASCII range */
    public static final int ASCII_INVALID_TO    = 199;

    /**
     * Mapping from character strings to their corresponding token type codes.
     * <p>
     * This map provides efficient lookup for converting character strings
     * to their corresponding token type constants used by the lexer.
     * </p>
     */
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
