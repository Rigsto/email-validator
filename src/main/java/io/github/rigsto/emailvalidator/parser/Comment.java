package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.parser.strategy.CommentStrategy;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.reason.UnopenedComment;
import io.github.rigsto.emailvalidator.result.reason.UnclosedComment;
import io.github.rigsto.emailvalidator.warning.QuotedPart;

import java.util.Arrays;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Parser for comments in email addresses.
 * <p>
 * Comments in email addresses are enclosed in parentheses and can appear
 * in various parts of the email. This parser uses a strategy pattern to
 * handle different comment parsing rules for different contexts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class Comment extends PartParser {
    /**
     * Counter for opened parentheses in nested comments.
     */
    private int openedParenthesis = 0;
    
    /**
     * The strategy used for parsing comments in different contexts.
     */
    private final CommentStrategy commentStrategy;

    /**
     * Creates a new Comment parser with the specified lexer and strategy.
     * 
     * @param lexer the lexer to use for tokenization
     * @param commentStrategy the strategy for parsing comments
     */
    public Comment(EmailLexer lexer, CommentStrategy commentStrategy) {
        super(lexer);
        this.commentStrategy = commentStrategy;
    }

    /**
     * Parses comments in email addresses.
     * <p>
     * Handles nested comments, validates comment syntax, and uses the
     * provided strategy for context-specific validation rules.
     * </p>
     * 
     * @return ValidEmail if comments are valid, InvalidEmail otherwise
     */
    @Override
    public Result parse() {
        if (this.lexer.current.isA(S_OPENPARENTHESIS)) {
            this.openedParenthesis++;

            if (this.noClosingParenthesis()) {
                return new InvalidEmail(new UnclosedComment(), this.lexer.current.value);
            }
        }

        if (this.lexer.current.isA(S_CLOSEPARENTHESIS)) {
            return new InvalidEmail(new UnopenedComment(), this.lexer.current.value);
        }

        this.warnings.add(new io.github.rigsto.emailvalidator.warning.Comment());

        boolean moreTokens = true;

        while (this.commentStrategy.exitCondition(this.lexer, this.openedParenthesis) && moreTokens) {
            if (this.lexer.isNextToken(S_OPENPARENTHESIS)) {
                this.openedParenthesis++;
            }
            this.warnEscaping();
            if (this.lexer.isNextToken(S_CLOSEPARENTHESIS)) {
                this.openedParenthesis--;
            }
            moreTokens = this.lexer.moveNext();
        }

        if (this.openedParenthesis >= 1) {
            return new InvalidEmail(new UnopenedComment(), this.lexer.current.value);
        }

        if (this.openedParenthesis < 0) {
            return new InvalidEmail(new UnopenedComment(), this.lexer.current.value);
        }

        Result finalValidations = this.commentStrategy.endOfLoopValidations(this.lexer);
        this.warnings.addAll(this.commentStrategy.getWarnings());

        return finalValidations;
    }

    private void warnEscaping() {
        if (!this.lexer.current.isA(S_BACKSLASH)) {
            return;
        }

        if (!this.lexer.isNextTokenAny(Arrays.asList(S_SP, S_HTAB, C_DEL))) {
            return;
        }

        this.warnings.add(new QuotedPart(this.lexer.getPrevious().type, this.lexer.current.type));
    }

    private boolean noClosingParenthesis() {
        try {
            this.lexer.find(S_CLOSEPARENTHESIS);
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }
}
