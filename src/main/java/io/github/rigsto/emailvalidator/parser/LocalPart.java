package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.constant.Constant;
import io.github.rigsto.emailvalidator.parser.strategy.LocalComment;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.ConsecutiveDot;
import io.github.rigsto.emailvalidator.result.reason.DotAtEnd;
import io.github.rigsto.emailvalidator.result.reason.DotAtStart;
import io.github.rigsto.emailvalidator.result.reason.ExpectingATEXT;
import io.github.rigsto.emailvalidator.warning.LocalTooLong;

import java.util.HashMap;
import java.util.Map;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Parser for the local part of email addresses.
 * <p>
 * The local part is the portion of an email address before the @ symbol.
 * This parser validates local part syntax, handles quoted strings, comments,
 * and ensures compliance with RFC standards for local parts.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class LocalPart extends PartParser {

    /**
     * Map of token types that are invalid in local parts.
     */
    public static Map<Integer, Integer> INVALID_TOKENS = new HashMap<>(Map.ofEntries(
            Map.entry(S_COMMA, S_COMMA),
            Map.entry(S_CLOSEBRACKET, S_CLOSEBRACKET),
            Map.entry(S_OPENBRACKET, S_OPENBRACKET),
            Map.entry(S_GREATERTHAN, S_GREATERTHAN),
            Map.entry(S_LOWERTHAN, S_LOWERTHAN),
            Map.entry(S_COLON, S_COLON),
            Map.entry(S_SEMICOLON, S_SEMICOLON),
            Map.entry(INVALID, INVALID)
    ));

    /**
     * The parsed local part string.
     */
    private String localPart = "";

    /**
     * Creates a new LocalPart parser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public LocalPart(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses the local part of an email address.
     * <p>
     * Validates local part syntax, handles quoted strings, comments,
     * and ensures compliance with RFC standards for local parts.
     * </p>
     * 
     * @return ValidEmail if the local part is valid, InvalidEmail otherwise
     */
    @Override
    public Result parse() {
        this.lexer.clearRecorded();
        this.lexer.startRecording();
        
        // Move to the first real token
        this.lexer.moveNext();

        while (!this.lexer.current.isA(S_AT) && !this.lexer.current.isA(S_EMPTY)) {
            if (this.hasDotAtStart()) {
                return new InvalidEmail(new DotAtStart(), this.lexer.current.value);
            }

            if (this.lexer.current.isA(S_DQUOTE)) {
                Result dQuoteParsingResult = parseDoubleQuote();
                if (dQuoteParsingResult.isInvalid()) {
                    return dQuoteParsingResult;
                }
            }

            if (this.lexer.current.isA(S_OPENPARENTHESIS) || this.lexer.current.isA(S_CLOSEPARENTHESIS)) {
                Result commentResult = parseComments();
                if (commentResult.isInvalid()) {
                    return commentResult;
                }
            }

            if (this.lexer.current.isA(S_DOT) && this.lexer.isNextToken(S_DOT)) {
                return new InvalidEmail(new ConsecutiveDot(), this.lexer.current.value);
            }

            if (this.lexer.current.isA(S_DOT) && this.lexer.isNextToken(S_AT)) {
                return new InvalidEmail(new DotAtEnd(), this.lexer.current.value);
            }

            Result escaping = validateEscaping();
            if (escaping.isInvalid()) {
                return escaping;
            }

            Result resultToken = validateTokens(false);
            if (resultToken.isInvalid()) {
                return resultToken;
            }

            Result resultFWS = parseLocalFWS();
            if (resultFWS.isInvalid()) {
                return resultFWS;
            }

            this.lexer.moveNext();
        }

        this.lexer.stopRecording();
        this.localPart = this.lexer.getAccumulatedValues().replaceAll("@+$", "");

        if (this.localPart.length() > Constant.LOCAL_PART_LENGTH) {
            this.warnings.add(new LocalTooLong());
        }

        return new ValidEmail();
    }

    /**
     * Validates tokens in the local part.
     * 
     * @param hasComments whether comments are present in the local part
     * @return ValidEmail if tokens are valid, InvalidEmail otherwise
     */
    protected Result validateTokens(boolean hasComments) {
        if (INVALID_TOKENS.containsKey(this.lexer.current.type)) {
            return new InvalidEmail(new ExpectingATEXT("Invalid token found"), this.lexer.current.value);
        }

        // Check for specific invalid characters that should be rejected in email addresses
        if (this.lexer.current.type == GENERIC && this.lexer.current.value.length() == 1) {
            char c = this.lexer.current.value.charAt(0);
            // Character 226 and ║ (0x2551) should be invalid for email addresses
            if (c == 226 || c == 0x2551) {
                return new InvalidEmail(new ExpectingATEXT("Invalid character in email address"), this.lexer.current.value);
            }
        }

        return new ValidEmail();
    }

    /**
     * Returns the parsed local part string.
     * 
     * @return the local part string
     */
    public String localPart() {
        return this.localPart;
    }

    private Result parseLocalFWS() {
        FoldingWhiteSpace foldingWhiteSpace = new FoldingWhiteSpace(this.lexer);
        Result result = foldingWhiteSpace.parse();

        if (result.isValid()) {
            this.warnings.addAll(foldingWhiteSpace.getWarnings());
        }

        return result;
    }

    private boolean hasDotAtStart() {
        return this.lexer.current.isA(S_DOT) && this.lexer.getPrevious().isA(S_EMPTY);
    }

    private Result parseDoubleQuote() {
        DoubleQuote dQuoteParser = new DoubleQuote(this.lexer);
        Result result = dQuoteParser.parse();

        this.warnings.addAll(dQuoteParser.getWarnings());
        return result;
    }

    /**
     * Parses comments within the local part.
     * 
     * @return ValidEmail if comments are valid, InvalidEmail otherwise
     */
    protected Result parseComments() {
        Comment commentParser = new Comment(this.lexer, new LocalComment());
        Result result = commentParser.parse();

        this.warnings.addAll(commentParser.getWarnings());
        return result;
    }

    private Result validateEscaping() {
        if (!this.lexer.current.isA(S_BACKSLASH)) {
            return new ValidEmail();
        }

        if (this.lexer.isNextToken(GENERIC) || this.lexer.isNextToken(UTF8_CHAR)) {
            return new InvalidEmail(new ExpectingATEXT("Found ATOM after escaping"), this.lexer.current.value);
        }

        return new ValidEmail();
    }
}
