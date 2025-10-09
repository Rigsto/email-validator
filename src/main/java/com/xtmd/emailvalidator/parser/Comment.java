package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.parser.strategy.CommentStrategy;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.reason.UnOpenedComment;
import com.xtmd.emailvalidator.result.reason.UnclosedComment;
import com.xtmd.emailvalidator.warning.QuotedPart;

import java.util.Arrays;

import static com.xtmd.emailvalidator.constant.LexerConstant.*;

public class Comment extends PartParser {
    private int openedParenthesis = 0;
    private final CommentStrategy commentStrategy;

    public Comment(EmailLexer lexer, CommentStrategy commentStrategy) {
        super(lexer);
        this.commentStrategy = commentStrategy;
    }

    @Override
    public Result parse() {
        if (this.lexer.current.isA(S_OPENPARENTHESIS)) {
            this.openedParenthesis++;

            if (this.noClosingParenthesis()) {
                return new InvalidEmail(new UnclosedComment(), this.lexer.current.value);
            }
        }

        if (this.lexer.current.isA(S_CLOSEPARENTHESIS)) {
            return new InvalidEmail(new UnOpenedComment(), this.lexer.current.value);
        }

        this.warnings.add(new com.xtmd.emailvalidator.warning.Comment());

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
            return new InvalidEmail(new UnOpenedComment(), this.lexer.current.value);
        }

        if (this.openedParenthesis < 0) {
            return new InvalidEmail(new UnOpenedComment(), this.lexer.current.value);
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
