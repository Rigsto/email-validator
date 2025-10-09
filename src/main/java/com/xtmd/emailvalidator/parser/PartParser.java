package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.constant.LexerConstant;
import com.xtmd.emailvalidator.lexer.Token;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ConsecutiveDot;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.HashSet;
import java.util.Set;

abstract class PartParser {
    protected Set<Warning> warnings = new HashSet<>();
    protected EmailLexer lexer;

    public PartParser(EmailLexer lexer) {
        this.lexer = lexer;
    }

    abstract public Result parse();

    public Set<Warning> getWarnings() {
        return this.warnings;
    }

    protected Result parseFWS() {
        PartParser foldingWS = new FoldingWhiteSpace(this.lexer);
        Result resultFWS = foldingWS.parse();

        this.warnings.addAll(foldingWS.getWarnings());
        return resultFWS;
    }

    protected Result checkConsecutiveDots() {
        if (this.lexer.current.isA(LexerConstant.S_DOT) && this.lexer.isNextToken(LexerConstant.S_DOT)) {
            return new InvalidEmail(new ConsecutiveDot(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    protected boolean escaped() {
        Token<Integer, String> previous = this.lexer.getPrevious();

        return previous.isA(LexerConstant.S_BACKSLASH)
                && !this.lexer.current.isA(LexerConstant.GENERIC);
    }
}
