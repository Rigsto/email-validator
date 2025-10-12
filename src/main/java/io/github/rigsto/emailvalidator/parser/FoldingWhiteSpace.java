package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.lexer.Token;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.*;
import io.github.rigsto.emailvalidator.warning.CFWSNearAt;
import io.github.rigsto.emailvalidator.warning.CFWSWithFWS;

import java.util.Arrays;
import java.util.List;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Parser for folding whitespace (FWS) in email addresses.
 * <p>
 * Folding whitespace consists of whitespace characters that can be used
 * for line folding in email addresses according to RFC standards. This parser
 * validates FWS syntax and generates appropriate warnings for deprecated usage.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class FoldingWhiteSpace extends PartParser {

    /**
     * List of token types that constitute folding whitespace.
     */
    public static final List<Integer> FWS_TYPES = Arrays.asList(S_SP, S_HTAB, S_CR, S_LF, CRLF);

    /**
     * Creates a new FoldingWhiteSpace parser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public FoldingWhiteSpace(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses folding whitespace in the input.
     * <p>
     * Validates FWS syntax, checks for CRLF sequences, and generates
     * appropriate warnings for deprecated usage patterns.
     * </p>
     * 
     * @return ValidEmail if FWS is valid, InvalidEmail otherwise
     */
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

    /**
     * Checks for valid CRLF sequences in folding whitespace.
     * <p>
     * CRLF sequences in FWS must be followed by whitespace characters
     * according to RFC standards.
     * </p>
     * 
     * @return ValidEmail if CRLF is valid, InvalidEmail otherwise
     */
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

    /**
     * Checks if the current token represents folding whitespace.
     * 
     * @return true if the current token is FWS, false otherwise
     */
    protected boolean isFWS() {
        if (this.escaped()) {
            return false;
        }

        return FWS_TYPES.contains(this.lexer.current.type);
    }
}
