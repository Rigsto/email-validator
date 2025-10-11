package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ExpectingATEXT;

import java.util.HashMap;
import java.util.Map;

import static com.xtmd.emailvalidator.constant.LexerConstant.*;

public class IDRightPart extends DomainPart {

    public IDRightPart(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    protected Result validateTokens(boolean hasComments) {
        Map<Integer, Boolean> invalidDomainTokens = new HashMap<>();
        invalidDomainTokens.put(S_DQUOTE, true);
        invalidDomainTokens.put(S_SQUOTE, true);
        invalidDomainTokens.put(S_BACKTICK, true);
        invalidDomainTokens.put(S_SEMICOLON, true);
        invalidDomainTokens.put(S_GREATERTHAN, true);
        invalidDomainTokens.put(S_LOWERTHAN, true);

        if (invalidDomainTokens.containsKey(this.lexer.current.type)) {
            return new InvalidEmail(new ExpectingATEXT("Invalid token in domain: " + this.lexer.current.value), this.lexer.current.value);
        }

        return new ValidEmail();
    }
}
