package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.CRLFX2;
import com.xtmd.emailvalidator.result.reason.CRNoLF;

import java.util.List;

public class FoldingWhiteSpace extends PartParser {

    private List<TokenInterface> tokenTypes;

    public FoldingWhiteSpace(EmailLexer lexer) {
        super(lexer);

        this.tokenTypes = List.of(
                Tokens.SP,
                Tokens.HTAB,
                Tokens.CR,
                Tokens.LF,
                Tokens.CRLF
        );
    }

    @Override
    public Result parse() {
        if (!this.isFWS()) {
            return new ValidEmail();
        }

        TokenInterface previous = this.lexer.getPrevious();

        Result resultCRLF = this.checkCRLFInFWS();
        if (resultCRLF.isInvalid()) {
            return resultCRLF;
        }

        if (this.lexer.getCurrent().equals(Tokens.CR)) {
            return new InvalidEmail(new CRNoLF(), this.lexer.getCurrent().getText());
        }

        return new ValidEmail();
    }

    protected Result checkCRLFInFWS() {
        if (!this.lexer.getCurrent().equals(Tokens.CRLF)) {
            return new ValidEmail();
        }

        if (!(this.lexer.isNextToken(Tokens.SP) || (this.lexer.isNextToken(Tokens.HTAB)))) {
            return new InvalidEmail(new CRLFX2(), this.lexer.getCurrent().getText());
        }

        return new ValidEmail();
    }

    protected boolean isFWS() {
        if (this.escaped()) {
            return false;
        }

        return this.tokenTypes.contains(this.lexer.getCurrent());
    }
}
