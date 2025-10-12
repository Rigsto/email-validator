package io.github.rigsto.emailvalidator.parser.strategy;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.constant.LexerConstant;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;
import io.github.rigsto.emailvalidator.warning.CFWSNearAt;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment parsing strategy for local parts of email addresses.
 * <p>
 * This strategy handles comment parsing in the local part of email addresses.
 * It checks for the @ symbol after comments and generates warnings for
 * deprecated folding whitespace near the @ symbol.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class LocalComment implements CommentStrategy {
    /**
     * List of warnings collected during comment parsing.
     */
    private final List<Warning> warningList = new ArrayList<>();

    /**
     * Determines the exit condition for local part comment parsing.
     * <p>
     * Exits when the next token is not an @ symbol, indicating the end
     * of the local part.
     * </p>
     * 
     * @param lexer the lexer being used for parsing
     * @param openedParenthesis the number of opened parentheses (unused)
     * @return true if parsing should exit, false to continue
     */
    @Override
    public boolean exitCondition(EmailLexer lexer, int openedParenthesis) {
        return !lexer.isNextToken(LexerConstant.S_AT);
    }

    /**
     * Performs validations at the end of local part comment parsing.
     * <p>
     * Checks that the next token is an @ symbol and adds a warning
     * for deprecated folding whitespace near the @ symbol.
     * </p>
     * 
     * @param lexer the lexer being used for parsing
     * @return ValidEmail if validation passes, InvalidEmail otherwise
     */
    @Override
    public Result endOfLoopValidations(EmailLexer lexer) {
        if (!lexer.isNextToken(LexerConstant.S_AT)) {
            return new InvalidEmail(new ExpectingATEXT("ATEXT is not expected after closing comments"), lexer.current.value);
        }

        this.warningList.add(new CFWSNearAt());
        return new ValidEmail();
    }

    /**
     * Returns the warnings collected during comment parsing.
     * 
     * @return a list of warnings
     */
    @Override
    public List<Warning> getWarnings() {
        return warningList;
    }
}
