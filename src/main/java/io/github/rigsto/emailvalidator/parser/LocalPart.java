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

public class LocalPart extends PartParser {

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

    private String localPart = "";

    public LocalPart(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    public Result parse() {
        this.lexer.clearRecorded();
        this.lexer.startRecording();

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

    protected Result validateTokens(boolean hasComments) {
        if (INVALID_TOKENS.containsKey(this.lexer.current.type)) {
            return new InvalidEmail(new ExpectingATEXT("Invalid token found"), this.lexer.current.value);
        }

        return new ValidEmail();
    }

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

        if (this.lexer.isNextToken(GENERIC)) {
            return new InvalidEmail(new ExpectingATEXT("Found ATOM after escaping"), this.lexer.current.value);
        }

        return new ValidEmail();
    }
}
