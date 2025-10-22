package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.constant.LexerConstant;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for email parsers.
 * <p>
 * This class provides the common structure and behavior for parsing email addresses
 * and message IDs. It defines the parsing workflow and manages warnings collection
 * during the parsing process.
 * </p>
 * <p>
 * Subclasses must implement the specific parsing logic for different parts of
 * email addresses (local part, domain part, etc.).
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
abstract class Parser {
    /**
     * Set of warnings collected during parsing.
     */
    protected Set<Warning> warnings = new HashSet<>();
    
    /**
     * The lexer used for tokenizing the input.
     */
    protected EmailLexer lexer;

    /**
     * Parses the right part of the email (domain part).
     * <p>
     * This method must be implemented by subclasses to handle
     * the specific parsing logic for the domain part.
     * </p>
     * 
     * @return the parsing result
     */
    abstract protected Result parseRightFromAt();
    
    /**
     * Parses the left part of the email (local part).
     * <p>
     * This method must be implemented by subclasses to handle
     * the specific parsing logic for the local part.
     * </p>
     * 
     * @return the parsing result
     */
    abstract protected Result parseLeftFromAt();
    
    /**
     * Performs pre-parsing validation before processing the left part.
     * <p>
     * This method must be implemented by subclasses to perform
     * any necessary validation before parsing begins.
     * </p>
     * 
     * @return the pre-parsing result
     */
    abstract protected Result preLeftParsing();

    /**
     * Creates a new Parser instance with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public Parser(EmailLexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Parses the input string according to email syntax rules.
     * <p>
     * This method orchestrates the parsing process by:
     * 1. Setting the input on the lexer
     * 2. Checking for invalid tokens
     * 3. Performing pre-parsing validation
     * 4. Parsing the left part (local part)
     * 5. Parsing the right part (domain part)
     * </p>
     * 
     * @param str the input string to parse
     * @return the parsing result (ValidEmail or InvalidEmail)
     */
    public Result parse(String str) {
        this.lexer.setInput(str);

        if (this.lexer.hasInvalidTokens()) {
            return new InvalidEmail(new ExpectingATEXT("Invalid tokens found"), this.lexer.current.value);
        }

        Result preParsingResult = preLeftParsing();
        if (preParsingResult.isInvalid()) {
            return preParsingResult;
        }

        // Move to the first real token before parsing
        this.lexer.moveNext();

        Result localPartResult = parseLeftFromAt();
        if (localPartResult.isInvalid()) {
            return localPartResult;
        }

        Result domainPartResult = parseRightFromAt();
        if (domainPartResult.isInvalid()) {
            return domainPartResult;
        }

        return new ValidEmail();
    }

    /**
     * Returns the warnings collected during parsing.
     * 
     * @return a set of warnings
     */
    public Set<Warning> getWarnings() {
        return this.warnings;
    }

    /**
     * Checks if the input contains an @ symbol.
     * <p>
     * This method checks if the input string contains an @ symbol
     * without moving the lexer position.
     * </p>
     * 
     * @return true if the input contains @, false otherwise
     */
    protected boolean hasAtToken() {
        // Check if the input contains @ symbol without moving lexer
        String input = this.lexer.getInput();
        return input != null && input.contains("@");
    }
}
