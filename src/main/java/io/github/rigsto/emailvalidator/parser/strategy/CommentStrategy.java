package io.github.rigsto.emailvalidator.parser.strategy;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.List;

/**
 * Strategy interface for parsing comments in different contexts.
 * <p>
 * This interface defines the contract for comment parsing strategies
 * that can be used in different parts of email addresses (local part,
 * domain part, etc.). Each strategy provides specific logic for
 * determining when to exit comment parsing and what validations
 * to perform.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public interface CommentStrategy {
    /**
     * Determines whether to exit the comment parsing loop.
     * 
     * @param lexer the lexer being used for parsing
     * @param openedParenthesis the number of opened parentheses
     * @return true if parsing should exit, false to continue
     */
    boolean exitCondition(EmailLexer lexer, int openedParenthesis);
    
    /**
     * Performs validations at the end of the comment parsing loop.
     * 
     * @param lexer the lexer being used for parsing
     * @return the validation result
     */
    Result endOfLoopValidations(EmailLexer lexer);
    
    /**
     * Returns the warnings collected during comment parsing.
     * 
     * @return a list of warnings
     */
    List<Warning> getWarnings();
}
