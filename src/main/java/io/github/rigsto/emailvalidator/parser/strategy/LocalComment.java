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
