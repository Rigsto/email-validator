package com.xtmd.emailvalidator.parser.strategy;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.List;

public interface CommentStrategy {
    boolean exitCondition(EmailLexer lexer, int openedParenthesis);
    Result endOfLoopValidations(EmailLexer lexer);
    List<Warning> getWarnings();
}
