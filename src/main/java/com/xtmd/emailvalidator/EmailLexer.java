package com.xtmd.emailvalidator;

import com.xtmd.emailvalidator.lexer.AbstractLexer;
import com.xtmd.emailvalidator.lexer.Token;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static com.xtmd.emailvalidator.constant.LexerConstant.*;

public class EmailLexer extends AbstractLexer<Integer, String> {

    public static final Pattern INVALID_CHARS_PATTERN = Pattern.compile(
            "[^\\p{S}\\p{C}\\p{Cc}]+",
            Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE
    );
    public static final Pattern VALID_UTF8_PATTERN = Pattern.compile(
            "\\p{Cc}+",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    public static final String[] CATCHABLE_PATTERNS = new String[] {
            "[a-zA-Z]+[46]?",   // ASCII and domain literal
            "[^\\x00-\\x7F]",   // UTF-8
            "[0-9]+",
            "\\r\\n",
            "::",
            "\\s+?",
            "."                 // any single char
    };

    public static final String[] NON_CATCHABLE_PATTERNS = new String[] {
            "[\\xA0-\\xff]+"
    };

    public static final String MODIFIERS = "iu";

    protected boolean hasInvalidTokens = false;

    protected Token<Integer, String> previous;
    public Token<Integer, String> current;

    private final Token<Integer, String> nullToken;

    private String accumulator = "";
    private boolean hasToRecord = false;

    public EmailLexer() {
        this.nullToken = new Token<>("", S_EMPTY, 0);
        this.current = this.previous = this.nullToken;
        this.lookahead = null;
    }

    @Override
    public void reset() {
        this.hasInvalidTokens = false;
        super.reset();
        this.current = this.previous = this.nullToken;
    }

    public boolean find(int type) {
        EmailLexer lexer = new EmailLexer();
        lexer.skipUntil(type);

        if (lexer.lookahead != null) {
            throw new NoSuchElementException(type + " not found");
        }

        return true;
    }

    @Override
    public boolean moveNext() {
        if (this.hasToRecord && this.previous == this.nullToken) {
            this.accumulator += this.current.value;
        }

        this.previous = this.current;

        if (this.lookahead == null) {
            this.lookahead = this.nullToken;
        }

        boolean hasNext = super.moveNext();
        this.current = (this.token != null) ? this.token : this.nullToken;

        if (this.hasToRecord) {
            this.accumulator += this.current.value;
        }

        return hasNext;
    }

    @Override
    protected String[] getCatchablePatterns() {
        return CATCHABLE_PATTERNS;
    }

    @Override
    protected String[] getNonCatchablePatterns() {
        return NON_CATCHABLE_PATTERNS;
    }

    @Override
    protected String getModifiers() {
        return MODIFIERS;
    }

    @Override
    protected Integer getType(String value) {
        if (isValid(value)) {
            return charValue.get(value);
        }

        if (isNullType(value)) {
            return C_NUL;
        }

        if (isInvalidChar(value)) {
            this.hasInvalidTokens = true;
            return INVALID;
        }

        return GENERIC;
    }

    @Override
    protected String transformValue(String raw, Integer type) {
        return raw;
    }

    protected boolean isValid(String value) {
        return charValue.containsKey(value);
    }

    protected boolean isNullType(String value) {
        return "\0".equals(value);
    }

    protected boolean isInvalidChar(String value) {
        return !INVALID_CHARS_PATTERN.matcher(value).find();
    }

    protected boolean isUTF8Invalid(String value) {
        return VALID_UTF8_PATTERN.matcher(value).find();
    }

    public boolean hasInvalidTokens() {
        return this.hasInvalidTokens;
    }

    public Token<Integer, String> getPrevious() {
        return this.previous;
    }

    public String getAccumulatedValues() {
        return this.accumulator;
    }

    public void startRecording() {
        this.hasToRecord = true;
    }

    public void stopRecording() {
        this.hasToRecord = false;
    }

    public void clearRecorded() {
        this.accumulator = "";
    }
}
