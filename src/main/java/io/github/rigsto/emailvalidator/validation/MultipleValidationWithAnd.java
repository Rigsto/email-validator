package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.exception.EmptyValidationList;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.MultipleErrors;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validation strategy that combines multiple validation strategies using AND logic.
 * <p>
 * This validation runs multiple validation strategies in sequence and returns
 * true only if all validations pass. It can be configured to stop on the first
 * error or collect all errors from all validations.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class MultipleValidationWithAnd implements EmailValidation {

    /**
     * Mode constant for stopping validation on the first error.
     */
    public static final int STOP_ON_ERROR = 0;
    
    /**
     * Mode constant for allowing all errors to be collected.
     */
    public static final int ALLOW_ALL_ERRORS = 1;

    /**
     * Set of warnings collected from all validations.
     */
    private final Set<Warning> warnings = new HashSet<>();
    
    /**
     * Combined error from multiple validations.
     */
    private MultipleErrors error = null;

    /**
     * List of validation strategies to run.
     */
    private final List<EmailValidation> validations;
    
    /**
     * The validation mode (stop on error or collect all errors).
     */
    private final int mode;

    /**
     * Creates a new MultipleValidationWithAnd with the specified validations and mode.
     * 
     * @param validations the list of validation strategies to run
     * @param mode the validation mode (STOP_ON_ERROR or ALLOW_ALL_ERRORS)
     * @throws EmptyValidationList if the validations list is null or empty
     */
    public MultipleValidationWithAnd(List<EmailValidation> validations, int mode) throws EmptyValidationList {
        if (validations == null || validations.isEmpty()) {
            throw new EmptyValidationList();
        }

        this.validations = validations;
        this.mode = mode;
    }

    /**
     * Creates a new MultipleValidationWithAnd with the specified validations.
     * <p>
     * Uses ALLOW_ALL_ERRORS mode by default.
     * </p>
     * 
     * @param validations the list of validation strategies to run
     * @throws EmptyValidationList if the validations list is null or empty
     */
    public MultipleValidationWithAnd(List<EmailValidation> validations) throws EmptyValidationList {
        this(validations, ALLOW_ALL_ERRORS);
    }

    @Override
    public boolean isValid(String email, EmailLexer emailLexer) {
        boolean result = true;

        for (EmailValidation validation : validations) {
            emailLexer.reset();

            boolean validationResult = validation.isValid(email, emailLexer);
            result = result && validationResult;

            this.warnings.addAll(validation.getWarnings());

            if (!validationResult) {
                processError(validation);
            }

            if (shouldStop(result)) {
                break;
            }
        }

        return result;
    }

    private void processError(EmailValidation validation) {
        InvalidEmail ve = validation.getError();
        if (ve != null) {
            initErrorStorage();
            this.error.addReason(ve.getReason());
        }
    }

    private void initErrorStorage() {
        if (this.error == null) {
            this.error = new MultipleErrors();
        }
    }

    private boolean shouldStop(boolean currentResult) {
        return !currentResult && this.mode == STOP_ON_ERROR;
    }

    @Override
    public InvalidEmail getError() {
        return this.error;
    }

    @Override
    public List<Warning> getWarnings() {
        return new ArrayList<>(this.warnings);
    }
}
