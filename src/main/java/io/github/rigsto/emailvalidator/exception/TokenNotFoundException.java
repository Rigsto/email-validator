package io.github.rigsto.emailvalidator.exception;

/**
 * Exception thrown when a requested token is not found during lexer operations.
 * <p>
 * This exception is used by the lexer when attempting to find a specific
 * token type that doesn't exist in the current input stream.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class TokenNotFoundException extends RuntimeException{
}
