package com.xtmd.emailvalidator;

import com.xtmd.emailvalidator.constant.LexerConstant;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ExpectingATEXT;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.HashSet;
import java.util.Set;

abstract class Parser {
    protected Set<Warning> warnings = new HashSet<>();
    protected EmailLexer lexer;

    abstract protected Result parseRightFromAt();
    abstract protected Result parseLeftFromAt();
    abstract protected Result preLeftParsing();

    public Parser(EmailLexer lexer) {
        this.lexer = lexer;
    }

    public Result parse(String str) {
        this.lexer.setInput(str);

        if (this.lexer.hasInvalidTokens()) {
            return new InvalidEmail(new ExpectingATEXT("Invalid tokens found"), this.lexer.current.value);
        }

        Result preParsingResult = preLeftParsing();
        if (preParsingResult.isInvalid()) {
            return preParsingResult;
        }

        Result localPartResult = parseLeftFromAt();
        if (localPartResult.isInvalid()) {
            return localPartResult;
        }

        Result domainPartResult = parseRightFromAt();
        if (domainPartResult.isInvalid()) {
            return domainPartResult;
        }

        return new ValidEmail();
    }

    public Set<Warning> getWarnings() {
        return this.warnings;
    }

    protected boolean hasAtToken() {
        this.lexer.moveNext();
        this.lexer.moveNext();

        return !this.lexer.current.isA(LexerConstant.S_AT);
    }
}
