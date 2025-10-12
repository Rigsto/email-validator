package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.lexer.AbstractLexer;
import io.github.rigsto.emailvalidator.lexer.Token;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Lexer for tokenizing email addresses according to email syntax rules.
 * <p>
 * This class extends AbstractLexer to provide specialized tokenization for email addresses.
 * It handles various character types including ASCII, UTF-8, domain literals, and special
 * characters used in email addresses.
 * </p>
 * <p>
 * The lexer uses regex patterns to identify different types of tokens and provides
 * functionality for recording token sequences and detecting invalid characters.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class EmailLexer extends AbstractLexer<Integer, String> {

    /**
     * Pattern for identifying invalid characters in email addresses.
     */
    public static final Pattern INVALID_CHARS_PATTERN = Pattern.compile(
            "[^\\p{S}\\p{C}\\p{Cc}]+",
            Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE
    );
    
    /**
     * Pattern for identifying valid UTF-8 control characters.
     */
    public static final Pattern VALID_UTF8_PATTERN = Pattern.compile(
            "\\p{Cc}+",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    /**
     * Regex patterns for catchable tokens in email addresses.
     */
    public static final String[] CATCHABLE_PATTERNS = new String[] {
            "[a-zA-Z]+[46]?",   // ASCII and domain literal
            "[^\\x00-\\x7F]",   // UTF-8
            "[0-9]+",
            "\\r\\n",
            "::",
            "\\s+?",
            "."                 // any single char
    };

    /**
     * Regex patterns for non-catchable tokens in email addresses.
     */
    public static final String[] NON_CATCHABLE_PATTERNS = new String[] {
            "[\\xA0-\\xff]+"
    };

    /**
     * Regex modifiers used for pattern matching.
     */
    public static final String MODIFIERS = "iu";

    /**
     * Flag indicating whether invalid tokens have been encountered.
     */
    protected boolean hasInvalidTokens = false;

    /**
     * The previous token processed by the lexer.
     */
    protected Token<Integer, String> previous;
    
    /**
     * The current token being processed by the lexer.
     */
    public Token<Integer, String> current;

    /**
     * A null token used as a placeholder.
     */
    private final Token<Integer, String> nullToken;

    /**
     * Accumulator for recording token values.
     */
    private String accumulator = "";
    
    /**
     * Flag indicating whether token recording is active.
     */
    private boolean hasToRecord = false;

    /**
     * Creates a new EmailLexer instance.
     * <p>
     * Initializes the lexer with null tokens and resets all state.
     * </p>
     */
    public EmailLexer() {
        this.nullToken = new Token<>("", S_EMPTY, 0);
        this.current = this.previous = this.nullToken;
        this.lookahead = null;
    }

    /**
     * Resets the lexer to its initial state.
     * <p>
     * Clears invalid token flags and resets all token references.
     * </p>
     */
    @Override
    public void reset() {
        this.hasInvalidTokens = false;
        super.reset();
        this.current = this.previous = this.nullToken;
    }

    /**
     * Searches for a token of the specified type.
     * <p>
     * Creates a new lexer instance and searches for the specified token type.
     * Throws an exception if the token type is not found.
     * </p>
     * 
     * @param type the token type to search for
     * @return true if the token type is found
     * @throws NoSuchElementException if the token type is not found
     */
    public boolean find(int type) {
        EmailLexer lexer = new EmailLexer();
        lexer.skipUntil(type);

        if (lexer.lookahead != null) {
            throw new NoSuchElementException(type + " not found");
        }

        return true;
    }

    /**
     * Moves to the next token in the input stream.
     * <p>
     * Updates the current and previous token references, and accumulates
     * token values if recording is active.
     * </p>
     * 
     * @return true if there is a next token, false otherwise
     */
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

    /**
     * Returns the catchable patterns used for tokenization.
     * 
     * @return array of catchable regex patterns
     */
    @Override
    protected String[] getCatchablePatterns() {
        return CATCHABLE_PATTERNS;
    }

    /**
     * Returns the non-catchable patterns used for tokenization.
     * 
     * @return array of non-catchable regex patterns
     */
    @Override
    protected String[] getNonCatchablePatterns() {
        return NON_CATCHABLE_PATTERNS;
    }

    /**
     * Returns the regex modifiers used for pattern matching.
     * 
     * @return the regex modifiers string
     */
    @Override
    protected String getModifiers() {
        return MODIFIERS;
    }

    /**
     * Determines the token type for a given value.
     * <p>
     * Analyzes the input value and returns the appropriate token type
     * based on character validity and special character detection.
     * </p>
     * 
     * @param value the input value to analyze
     * @return the token type for the value
     */
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

    /**
     * Transforms the raw token value.
     * <p>
     * In this implementation, no transformation is applied to the raw value.
     * </p>
     * 
     * @param raw the raw token value
     * @param type the token type
     * @return the transformed value (same as raw in this case)
     */
    @Override
    protected String transformValue(String raw, Integer type) {
        return raw;
    }

    /**
     * Checks if a value represents a valid character.
     * 
     * @param value the value to check
     * @return true if the value is valid, false otherwise
     */
    protected boolean isValid(String value) {
        return charValue.containsKey(value);
    }

    /**
     * Checks if a value represents a null character.
     * 
     * @param value the value to check
     * @return true if the value is null, false otherwise
     */
    protected boolean isNullType(String value) {
        return "\0".equals(value);
    }

    /**
     * Checks if a value represents an invalid character.
     * 
     * @param value the value to check
     * @return true if the value is invalid, false otherwise
     */
    protected boolean isInvalidChar(String value) {
        return !INVALID_CHARS_PATTERN.matcher(value).find();
    }

    /**
     * Checks if a value represents an invalid UTF-8 character.
     * 
     * @param value the value to check
     * @return true if the value is invalid UTF-8, false otherwise
     */
    protected boolean isUTF8Invalid(String value) {
        return VALID_UTF8_PATTERN.matcher(value).find();
    }

    /**
     * Checks if invalid tokens have been encountered during tokenization.
     * 
     * @return true if invalid tokens were found, false otherwise
     */
    public boolean hasInvalidTokens() {
        return this.hasInvalidTokens;
    }

    /**
     * Returns the previous token processed by the lexer.
     * 
     * @return the previous token
     */
    public Token<Integer, String> getPrevious() {
        return this.previous;
    }

    /**
     * Returns the accumulated values from token recording.
     * 
     * @return the accumulated string of recorded values
     */
    public String getAccumulatedValues() {
        return this.accumulator;
    }

    /**
     * Starts recording token values.
     * <p>
     * When recording is active, token values are accumulated in the accumulator.
     * </p>
     */
    public void startRecording() {
        this.hasToRecord = true;
    }

    /**
     * Stops recording token values.
     */
    public void stopRecording() {
        this.hasToRecord = false;
    }

    /**
     * Clears the accumulated recorded values.
     */
    public void clearRecorded() {
        this.accumulator = "";
    }
}
