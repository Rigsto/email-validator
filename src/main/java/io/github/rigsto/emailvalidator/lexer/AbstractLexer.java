package io.github.rigsto.emailvalidator.lexer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract base class for lexical analyzers (lexers).
 * <p>
 * This class provides the core functionality for tokenizing input strings
 * using regular expressions. It manages token generation, position tracking,
 * and provides utilities for token navigation and pattern matching.
 * </p>
 * <p>
 * Subclasses must implement the abstract methods to define specific
 * tokenization patterns and type determination logic.
 * </p>
 * 
 * @param <T> the type of token type identifiers
 * @param <V> the type of token values
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public abstract class AbstractLexer<T, V> {
    /**
     * The input string being tokenized.
     */
    private String input = "";
    
    /**
     * List of tokens generated from the input.
     */
    private final List<Token<T, V>> tokens = new ArrayList<>();
    
    /**
     * Current position in the token list.
     */
    private int position = 0;
    
    /**
     * Peek position for lookahead operations.
     */
    private int peek = 0;

    /**
     * The next token to be processed (lookahead).
     */
    public Token<T, V> lookahead;
    
    /**
     * The current token being processed.
     */
    public Token<T, V> token;
    
    /**
     * The compiled regex pattern used for tokenization.
     */
    public Pattern regex;

    /**
     * Sets the input string and performs initial tokenization.
     * <p>
     * This method clears any existing tokens, resets the lexer state,
     * and scans the new input to generate tokens.
     * </p>
     * 
     * @param input the input string to tokenize
     */
    public void setInput(String input) {
        this.input = (input != null)  ? input : "";
        this.tokens.clear();

        reset();
        scan(this.input);
    }

    /**
     * Resets the lexer to its initial state.
     * <p>
     * Clears the lookahead and current token, and resets position counters.
     * </p>
     */
    public void reset() {
        this.lookahead = null;
        this.token = null;
        this.peek = 0;
        this.position = 0;
    }

    /**
     * Resets the peek position to zero.
     */
    public void resetPeek() {
        this.peek = 0;
    }

    /**
     * Resets the position to a valid range.
     */
    public void resetPosition() {
        this.position = Math.max(0, position);
    }

    /**
     * Returns a substring of the input up to the specified position.
     * 
     * @param position the end position (exclusive)
     * @return the substring from start to the specified position
     */
    public String getInputUntilPosition(int position) {
        int end = Math.max(0, Math.min(position, input.length()));
        return input.substring(0, end);
    }

    /**
     * Returns the input string being tokenized.
     * 
     * @return the input string
     */
    public String getInput() {
        return this.input;
    }

    /**
     * Checks if the next token matches the specified type.
     * 
     * @param type the token type to check
     * @return true if the next token matches the type, false otherwise
     */
    public boolean isNextToken(T type) {
        return this.lookahead != null && this.lookahead.isA(type);
    }

    /**
     * Checks if the next token matches any of the specified types.
     * 
     * @param types the list of token types to check against
     * @return true if the next token matches any of the types, false otherwise
     */
    public boolean isNextTokenAny(List<T> types) {
        if (this.lookahead == null || types == null || types.isEmpty()) {
            return false;
        }

        for (T type : types) {
            if (this.lookahead.isA(type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Moves to the next token in the sequence.
     * <p>
     * Updates the current token to the lookahead token and advances
     * to the next token in the list.
     * </p>
     * 
     * @return true if there is a next token, false if at the end
     */
    public boolean moveNext() {
        this.peek = 0;
        this.token = this.lookahead;
        this.lookahead = (this.position < this.tokens.size())
                ? this.tokens.get(this.position++)
                : null;

        return this.lookahead != null;
    }

    /**
     * Skips tokens until finding one of the specified type.
     * 
     * @param type the token type to stop at
     */
    public void skipUntil(T type) {
        while (this.lookahead != null && !this.lookahead.isA(type)) {
            moveNext();
        }
    }

    /**
     * Checks if a value matches the specified token type.
     * 
     * @param value the value to check
     * @param tokenType the token type to match against
     * @return true if the value matches the token type, false otherwise
     */
    public boolean isA(String value, T tokenType) {
        T derived = getType(value);
        return Objects.equals(derived, tokenType);
    }

    /**
     * Peeks at the next token without advancing the position.
     * <p>
     * This method allows looking ahead at future tokens without
     * consuming them. The peek counter is incremented for each call.
     * </p>
     * 
     * @return the next token, or null if at the end
     */
    public Token<T, V> peek() {
        int idx = this.position + this.peek;

        if (idx >= 0 && idx < this.tokens.size()) {
            this.peek++;
            return this.tokens.get(idx);
        }

        return null;
    }

    /**
     * Takes a single glimpse at the next token and resets peek position.
     * 
     * @return the next token, or null if at the end
     */
    public Token<T, V> glimpse() {
        Token<T, V> p = peek();
        this.peek = 0;
        return p;
    }

    /**
     * Scans the input string and generates tokens using regex patterns.
     * <p>
     * This method compiles the regex pattern from catchable and non-catchable
     * patterns, then applies it to the input to generate tokens.
     * </p>
     * 
     * @param input the input string to scan
     */
    protected void scan(String input) {
        if (this.regex == null) {
            String[] catchables = getCatchablePatterns();
            String[] nonCatchables = getNonCatchablePatterns();

            StringBuilder sb = new StringBuilder();

            if (catchables != null && catchables.length > 0) {
                sb.append("(")
                        .append(String.join(")|(", catchables))
                        .append(")");

                if (nonCatchables != null && nonCatchables.length > 0) {
                    sb.append("|");
                }
            }

            if (nonCatchables != null && nonCatchables.length > 0) {
                sb.append(String.join("|", nonCatchables));
            }

            int flags = toPatternFlags(getModifiers());
            this.regex = Pattern.compile(sb.toString(), flags);
        }

        Matcher matcher = this.regex.matcher(input);
        while (matcher.find()) {
            String firstMatch = matcher.group();
            int offset = matcher.start();

            T type = getType(firstMatch);
            V value = transformValue(firstMatch, type);

            this.tokens.add(new Token<>(value, type, offset));
        }
    }

    /**
     * Returns a string representation of a token type for debugging purposes.
     * <p>
     * Uses reflection to find the field name corresponding to the token type,
     * or returns the type's string representation if not found.
     * </p>
     * 
     * @param tokenType the token type to get the literal for
     * @return a string representation of the token type
     */
    public String getLiteral(T tokenType) {
        if (tokenType == null) return "null";

        if (tokenType instanceof Enum<?> e) {
            return e.getClass().getName() + "::" + e.name();
        }

        Class<?> clazz = this.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            int mods = f.getModifiers();

            if (!Modifier.isStatic(mods) || !Modifier.isFinal(mods) || !Modifier.isPublic(mods)) {
                continue;
            }

            try {
                Object val = f.get(null);
                if (Objects.equals(val, tokenType)) {
                    return clazz.getName() + "::" + f.getName();
                }
            } catch (IllegalAccessException ignored) {}
        }

        return tokenType.toString();
    }

    /**
     * Returns the regex modifiers used for pattern compilation.
     * <p>
     * Default implementation returns "iu" (case-insensitive and Unicode).
     * Subclasses can override this to provide different modifiers.
     * </p>
     * 
     * @return the regex modifiers string
     */
    protected String getModifiers() {
        return "iu";
    }

    /**
     * Converts modifier string to Pattern flags.
     * 
     * @param modifiers the modifier string
     * @return the corresponding Pattern flags
     */
    private static int toPatternFlags(String modifiers) {
        int flags = 0;

        if (modifiers == null) {
            return flags;
        }

        for (char c : modifiers.toCharArray()) {
            switch (c) {
                case 'i' -> {
                    flags |= Pattern.CASE_INSENSITIVE;
                    flags |= Pattern.UNICODE_CASE;
                }
                case 'u' -> {
                    flags |= Pattern.UNICODE_CHARACTER_CLASS;
                }
                case 'm' -> flags |= Pattern.MULTILINE;
                case 's' -> flags |= Pattern.DOTALL;
                default -> {}
            }
        }

        return flags;
    }

    /**
     * Returns the regex patterns for tokens that should be captured.
     * <p>
     * Subclasses must implement this method to define which patterns
     * should be captured as tokens during tokenization.
     * </p>
     * 
     * @return array of catchable regex patterns
     */
    protected abstract String[] getCatchablePatterns();
    
    /**
     * Returns the regex patterns for tokens that should not be captured.
     * <p>
     * These patterns are used to match content that should be ignored
     * during tokenization.
     * </p>
     * 
     * @return array of non-catchable regex patterns
     */
    protected abstract String[] getNonCatchablePatterns();
    
    /**
     * Determines the token type for a given input value.
     * <p>
     * Subclasses must implement this method to classify input values
     * into appropriate token types.
     * </p>
     * 
     * @param value the input value to classify
     * @return the token type for the value
     */
    protected abstract T getType(String value);

    /**
     * Transforms the raw token value.
     * <p>
     * Default implementation returns the raw value unchanged.
     * Subclasses can override this to perform value transformations.
     * </p>
     * 
     * @param raw the raw token value
     * @param type the token type
     * @return the transformed value
     */
    @SuppressWarnings("unchecked")
    protected V transformValue(String raw, T type) {
        return (V) raw;
    }
}
