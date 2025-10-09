package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ConsecutiveDot;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class PartParser {
    protected Set<Warning> warnings = new HashSet<>();
    protected EmailLexer lexer;

    public PartParser(EmailLexer lexer) {
        this.lexer = lexer;
    }

    abstract public Result parse();

    @SuppressWarnings("Since15")
    public List<Warning> getWarnings() {
        return this.warnings.stream().toList();
    }

    protected Result parseFWS() {
        PartParser foldingWS = new FoldingWhiteSpace(this.lexer);
        Result resultFWS = foldingWS.parse();

        this.warnings.addAll(foldingWS.getWarnings());
        return resultFWS;
    }

    protected Result checkConsecutiveDots() {
        if (this.lexer.getCurrent().equals(Tokens.DOT) && this.lexer.isNextToken(Tokens.DOT)) {
            return new InvalidEmail(new ConsecutiveDot(), this.lexer.getCurrent().getText());
        }

        return new ValidEmail();
    }

    protected boolean escaped() {
        TokenInterface previous = this.lexer.getPrevious();

        return previous.equals(Tokens.BACKSLASH) && !this.lexer.getCurrent().getName().equals(Tokens.GENERIC);
    }
}
