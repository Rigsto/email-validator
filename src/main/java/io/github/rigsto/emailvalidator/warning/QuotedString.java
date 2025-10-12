package io.github.rigsto.emailvalidator.warning;

/**
 * Warning for quoted strings found in email addresses.
 * <p>
 * This warning is generated when a quoted string is found between specific
 * tokens in an email address, which may indicate unusual formatting patterns.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class QuotedString extends Warning {
    /**
     * The unique warning code for quoted string warnings.
     */
    public static final int CODE = 11;

    /**
     * Creates a new quoted string warning.
     * <p>
     * Initializes the warning with a message indicating where the quoted
     * string was found between the specified tokens.
     * </p>
     * 
     * @param prevToken the token before the quoted string
     * @param nextToken the token after the quoted string
     */
    public QuotedString(Object prevToken, Object nextToken) {
        String prev = tokenToString(prevToken);
        String next = tokenToString(nextToken);

        this.message = "Quoted String found between " + prev + " and " + next;
    }

    /**
     * Converts a token object to its string representation.
     * <p>
     * Handles null tokens, enum tokens, and other object types.
     * </p>
     * 
     * @param token the token to convert
     * @return the string representation of the token
     */
    private String tokenToString(Object token) {
        if (token == null) return "null";
        if (token instanceof Enum<?>) return ((Enum<?>) token).name();
        return token.toString();
    }
}
