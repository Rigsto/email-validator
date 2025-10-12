package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;

import java.util.HashMap;
import java.util.Map;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Parser for the right part of Message-ID headers.
 * <p>
 * This class extends DomainPart to provide specific parsing logic for
 * Message-ID right parts. It inherits most functionality from DomainPart
 * but overrides token validation to disallow certain characters that are
 * not allowed in Message-ID domain parts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IDRightPart extends DomainPart {

    /**
     * Creates a new IDRightPart parser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public IDRightPart(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Validates tokens in Message-ID right parts.
     * <p>
     * This method checks for invalid tokens that are not allowed in
     * Message-ID domain parts, such as quotes, semicolons, and angle brackets.
     * </p>
     * 
     * @param hasComments whether comments are present (unused in this implementation)
     * @return ValidEmail if tokens are valid, InvalidEmail otherwise
     */
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
