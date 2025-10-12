package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;

import java.util.HashMap;
import java.util.Map;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

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
