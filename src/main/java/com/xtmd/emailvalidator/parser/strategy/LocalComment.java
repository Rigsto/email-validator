package com.xtmd.emailvalidator.parser.strategy;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.constant.LexerConstant;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ExpectingATEXT;
import com.xtmd.emailvalidator.warning.CFWSNearAt;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

public class LocalComment implements CommentStrategy {
    private final List<Warning> warningList = new ArrayList<>();

    @Override
    public boolean exitCondition(EmailLexer lexer, int openedParenthesis) {
        return !lexer.isNextToken(LexerConstant.S_AT);
    }

    @Override
    public Result endOfLoopValidations(EmailLexer lexer) {
        if (!lexer.isNextToken(LexerConstant.S_AT)) {
            return new InvalidEmail(new ExpectingATEXT("ATEXT is not expected after closing comments"), lexer.current.value);
        }

        this.warningList.add(new CFWSNearAt());
        return new ValidEmail();
    }

    @Override
    public List<Warning> getWarnings() {
        return warningList;
    }
}
