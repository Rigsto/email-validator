package io.github.rigsto.emailvalidator.parser.strategy;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.warning.Warning;

import java.util.List;

public interface CommentStrategy {
    boolean exitCondition(EmailLexer lexer, int openedParenthesis);
    Result endOfLoopValidations(EmailLexer lexer);
    List<Warning> getWarnings();
}
