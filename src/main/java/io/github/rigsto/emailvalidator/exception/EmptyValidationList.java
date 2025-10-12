package io.github.rigsto.emailvalidator.exception;

/**
 * Exception thrown when attempting to create a validation with an empty list of validators.
 * <p>
 * This exception is used by validation classes that require at least one
 * validation strategy to be provided.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class EmptyValidationList extends Exception {
}
