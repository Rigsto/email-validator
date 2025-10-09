package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.EmailLexer;
import com.xtmd.emailvalidator.lexer.Token;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.ExpectingATEXT;
import com.xtmd.emailvalidator.result.reason.UnclosedQuotedString;
import com.xtmd.emailvalidator.warning.CFWSWithFWS;
import com.xtmd.emailvalidator.warning.QuotedString;

import java.util.HashMap;
import java.util.Map;

import static com.xtmd.emailvalidator.constant.LexerConstant.*;

public class DoubleQuote extends PartParser {

    public DoubleQuote(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    public Result parse() {
        Result validQuotedString = checkDQuote();
        if (validQuotedString.isInvalid()) {
            return validQuotedString;
        }

        Map<Integer, Boolean> special = new HashMap<>();
        special.put(S_CR, true);
        special.put(S_HTAB, true);
        special.put(S_LF, true);

        Map<Integer, Boolean> invalid = new HashMap<>();
        invalid.put(C_NUL, true);
        invalid.put(S_HTAB, true);
        invalid.put(S_CR, true);
        invalid.put(S_LF, true);

        boolean setSpecialWarning = true;

        this.lexer.moveNext();

        while (!this.lexer.current.isA(S_DQUOTE) && !this.lexer.current.isA(S_EMPTY)) {
            if (special.containsKey(this.lexer.current.type) && setSpecialWarning) {
                this.warnings.add(new CFWSWithFWS());
                setSpecialWarning = false;
            }

            if (this.lexer.current.isA(S_BACKSLASH) && this.lexer.isNextToken(S_DQUOTE)) {
                this.lexer.moveNext();
            }

            this.lexer.moveNext();

            if (!this.escaped() && invalid.containsKey(this.lexer.current.type)) {
                return new InvalidEmail(new ExpectingATEXT("Expecting ATEXT between DQUOTE"), this.lexer.current.value);
            }
        }

        Token<Integer, String> prev = this.lexer.getPrevious();

        if (prev.isA(S_BACKSLASH)) {
            validQuotedString = checkDQuote();
            if (validQuotedString.isInvalid()) {
                return validQuotedString;
            }
        }

        if (!this.lexer.isNextToken(S_AT) && !prev.isA(S_BACKSLASH)) {
            return new InvalidEmail(new ExpectingATEXT("Expecting ATEXT between DQUOTE"), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    protected Result checkDQuote() {
        Token<Integer, String> prev = this.lexer.getPrevious();

        if (this.lexer.isNextToken(GENERIC) && prev.isA(GENERIC)) {
            String description = "https://tools.ietf.org/html/rfc5322#section-3.2.4 - quoted string should be a unit";
            return new InvalidEmail(new ExpectingATEXT(description), this.lexer.current.value);
        }

        try {
            this.lexer.find(S_DQUOTE);
        } catch (Exception e) {
            return new InvalidEmail(new UnclosedQuotedString(), this.lexer.current.value);
        }

        this.warnings.add(new QuotedString(prev.value, this.lexer.current.value));
        return new ValidEmail();
    }
}
