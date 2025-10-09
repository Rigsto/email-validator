package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.reason.CommentsInIDRight;

public class IDLeftPart extends LocalPart {

    public IDLeftPart(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    protected Result parseComments() {
        return new InvalidEmail(new CommentsInIDRight(), this.lexer.current.value);
    }
}
