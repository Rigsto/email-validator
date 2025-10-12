package io.github.rigsto.emailvalidator.lexer;

import java.util.Objects;

/**
 * Represents a token produced by the lexer during tokenization.
 * <p>
 * A token contains the actual value found in the input, its type classification,
 * and the position where it was found. This class is generic to support different
 * types of values and token types.
 * </p>
 * 
 * @param <T> the type of the token type identifier
 * @param <V> the type of the token value
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public final class Token<T, V> {
    /**
     * The actual value of the token as found in the input.
     */
    public final V value;
    
    /**
     * The type classification of this token.
     */
    public final T type;
    
    /**
     * The position in the input where this token was found.
     */
    public final int position;

    /**
     * Creates a new token with the specified value, type, and position.
     * 
     * @param value the token value
     * @param type the token type
     * @param position the position in the input
     */
    public Token(V value, T type, int position) {
        this.value = value;
        this.type = type;
        this.position = position;
    }

    /**
     * Checks if this token matches any of the specified types.
     * 
     * @param types the types to check against
     * @return true if this token matches any of the specified types, false otherwise
     */
    @SafeVarargs
    public final boolean isA(T... types) {
        if (types == null) return false;

        for (T type : types) {
            if (Objects.equals(this.type, type)) return true;
        }

        return false;
    }

    /**
     * Returns a string representation of this token.
     * 
     * @return a string containing the token's value, type, and position
     */
    @Override
    public String toString() {
        return "Token{value=" + value + ", type=" + type + ", position=" + position + '}';
    }
}
