package io.github.rigsto.emailvalidator.parser.strategy;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.constant.LexerConstant;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.List;

/**
 * Comment parsing strategy for domain parts of email addresses.
 * <p>
 * This strategy handles comment parsing in the domain part of email addresses.
 * It checks for dots after comments and ensures proper domain structure.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainComment implements CommentStrategy {

    /**
     * Determines the exit condition for domain part comment parsing.
     * <p>
     * Exits when all parentheses are closed and the next token is a dot,
     * indicating the end of a domain label.
     * </p>
     * 
     * @param lexer the lexer being used for parsing
     * @param openedParenthesis the number of opened parentheses
     * @return true if parsing should exit, false to continue
     */
    @Override
    public boolean exitCondition(EmailLexer lexer, int openedParenthesis) {
        return !(openedParenthesis == 0 && lexer.isNextToken(LexerConstant.S_DOT));
    }

    /**
     * Performs validations at the end of domain part comment parsing.
     * <p>
     * Checks that the next token is a dot, which is required after
     * comments in domain parts.
     * </p>
     * 
     * @param lexer the lexer being used for parsing
     * @return ValidEmail if validation passes, InvalidEmail otherwise
     */
    @Override
    public Result endOfLoopValidations(EmailLexer lexer) {
        if (!lexer.isNextToken(LexerConstant.S_DOT)) {
            return new InvalidEmail(new ExpectingATEXT("Dot not found near CLOSEPARENTHESIS"), lexer.current.value);
        }

        return new ValidEmail();
    }

    /**
     * Returns the warnings collected during comment parsing.
     * <p>
     * Domain comments don't generate warnings in this implementation.
     * </p>
     * 
     * @return an empty list of warnings
     */
    @Override
    public List<Warning> getWarnings() {
        return List.of();
    }
}
