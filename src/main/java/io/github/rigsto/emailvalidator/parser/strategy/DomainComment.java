package io.github.rigsto.emailvalidator.parser.strategy;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.constant.LexerConstant;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;
import io.github.rigsto.emailvalidator.warning.Warning;

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
