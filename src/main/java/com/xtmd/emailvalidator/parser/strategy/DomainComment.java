package com.xtmd.emailvalidator.parser.strategy;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.constant.LexerConstant;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ExpectingATEXT;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.List;

public class DomainComment implements CommentStrategy {

    @Override
    public boolean exitCondition(EmailLexer lexer, int openedParenthesis) {
        return !(openedParenthesis == 0 && lexer.isNextToken(LexerConstant.S_DOT));
    }

    @Override
    public Result endOfLoopValidations(EmailLexer lexer) {
        if (!lexer.isNextToken(LexerConstant.S_DOT)) {
            return new InvalidEmail(new ExpectingATEXT("Dot not found near CLOSEPARENTHESIS"), lexer.current.value);
        }

        return new ValidEmail();
    }

    @Override
    public List<Warning> getWarnings() {
        return List.of();
    }
}
