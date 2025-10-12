package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.reason.CommentsInIDRight;

public class IDLeftPart extends LocalPart {

    public IDLeftPart(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    protected Result parseComments() {
        return new InvalidEmail(new CommentsInIDRight(), this.lexer.current.value);
    }
}
