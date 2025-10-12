package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.constant.LexerConstant;
import io.github.rigsto.emailvalidator.lexer.Token;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ConsecutiveDot;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for parsing specific parts of email addresses.
 * <p>
 * This class provides common functionality for parsing different parts
 * of email addresses (local part, domain part, etc.). It includes
 * utilities for handling folding whitespace, checking for consecutive
 * dots, and managing warnings.
 * </p>
 * <p>
 * Subclasses must implement the parse() method to define specific
 * parsing logic for their respective parts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
abstract class PartParser {
    /**
     * Set of warnings collected during parsing.
     */
    protected Set<Warning> warnings = new HashSet<>();
    
    /**
     * The lexer used for tokenizing the input.
     */
    protected EmailLexer lexer;

    /**
     * Creates a new PartParser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public PartParser(EmailLexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Parses the input according to the specific part's rules.
     * <p>
     * This method must be implemented by subclasses to define
     * the specific parsing logic for their part type.
     * </p>
     * 
     * @return the parsing result
     */
    abstract public Result parse();

    /**
     * Returns the warnings collected during parsing.
     * 
     * @return a set of warnings
     */
    public Set<Warning> getWarnings() {
        return this.warnings;
    }

    /**
     * Parses folding whitespace (FWS) in the input.
     * <p>
     * FWS consists of whitespace characters that can be used
     * for line folding in email addresses.
     * </p>
     * 
     * @return the parsing result for FWS
     */
    protected Result parseFWS() {
        PartParser foldingWS = new FoldingWhiteSpace(this.lexer);
        Result resultFWS = foldingWS.parse();

        this.warnings.addAll(foldingWS.getWarnings());
        return resultFWS;
    }

    /**
     * Checks for consecutive dot characters, which are invalid in email addresses.
     * 
     * @return InvalidEmail if consecutive dots are found, ValidEmail otherwise
     */
    protected Result checkConsecutiveDots() {
        if (this.lexer.current.isA(LexerConstant.S_DOT) && this.lexer.isNextToken(LexerConstant.S_DOT)) {
            return new InvalidEmail(new ConsecutiveDot(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    /**
     * Checks if the current token is escaped by a backslash.
     * 
     * @return true if the current token is escaped, false otherwise
     */
    protected boolean escaped() {
        Token<Integer, String> previous = this.lexer.getPrevious();

        return previous.isA(LexerConstant.S_BACKSLASH)
                && !this.lexer.current.isA(LexerConstant.GENERIC);
    }
}
