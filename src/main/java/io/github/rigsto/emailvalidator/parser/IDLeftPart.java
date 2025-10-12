package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.reason.CommentsInIDRight;

/**
 * Parser for the left part of Message-ID headers.
 * <p>
 * This class extends LocalPart to provide specific parsing logic for
 * Message-ID left parts. It inherits most functionality from LocalPart
 * but overrides comment parsing to disallow comments in Message-ID headers.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class IDLeftPart extends LocalPart {

    /**
     * Creates a new IDLeftPart parser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public IDLeftPart(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses comments in Message-ID left parts.
     * <p>
     * Comments are not allowed in Message-ID headers, so this method
     * always returns an invalid result.
     * </p>
     * 
     * @return InvalidEmail indicating comments are not allowed
     */
    @Override
    protected Result parseComments() {
        return new InvalidEmail(new CommentsInIDRight(), this.lexer.current.value);
    }
}
