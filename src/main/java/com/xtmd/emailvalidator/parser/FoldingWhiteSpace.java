package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.lexer.Token;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.*;
import com.xtmd.emailvalidator.warning.CFWSNearAt;
import com.xtmd.emailvalidator.warning.CFWSWithFWS;

import java.util.Arrays;
import java.util.List;

import static com.xtmd.emailvalidator.constant.LexerConstant.*;

public class FoldingWhiteSpace extends PartParser {

    public static final List<Integer> FWS_TYPES = Arrays.asList(S_SP, S_HTAB, S_CR, S_LF, CRLF);

    public FoldingWhiteSpace(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    public Result parse() {
        if (!this.isFWS()) {
            return new ValidEmail();
        }

        Token<Integer, String> previous = this.lexer.getPrevious();

        Result resultCRLF = this.checkCRLFInFWS();
        if (resultCRLF.isInvalid()) {
            return resultCRLF;
        }

        if (this.lexer.current.isA(S_CR)) {
            return new InvalidEmail(new CRNoLF(), this.lexer.current.value);
        }

        if (this.lexer.isNextToken(GENERIC) && !previous.isA(S_AT)) {
            return new InvalidEmail(new AtextAfterCFWS(), this.lexer.current.value);
        }

        if (this.lexer.current.isA(S_LF) || this.lexer.current.isA(C_NUL)) {
            return new InvalidEmail(new ExpectingCTEXT(), this.lexer.current.value);
        }

        if (this.lexer.isNextToken(S_AT) || previous.isA(S_AT)) {
            this.warnings.add(new CFWSNearAt());
        } else {
            this.warnings.add(new CFWSWithFWS());
        }

        return new ValidEmail();
    }

    protected Result checkCRLFInFWS() {
        if (!this.lexer.current.isA(CRLF)) {
            return new ValidEmail();
        }

        if (!this.lexer.isNextTokenAny(Arrays.asList(S_SP, S_HTAB))) {
            return new InvalidEmail(new CRLFX2(), this.lexer.current.value);
        }

        if (!this.lexer.isNextTokenAny(Arrays.asList(S_SP, S_HTAB))) {
            return new InvalidEmail(new CRLFAtTheEnd(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    protected boolean isFWS() {
        if (this.escaped()) {
            return false;
        }

        return FWS_TYPES.contains(this.lexer.current.type);
    }
}
