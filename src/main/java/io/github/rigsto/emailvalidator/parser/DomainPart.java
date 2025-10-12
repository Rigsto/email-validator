package io.github.rigsto.emailvalidator.parser;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.lexer.Token;
import io.github.rigsto.emailvalidator.parser.strategy.DomainComment;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.*;
import io.github.rigsto.emailvalidator.warning.DeprecatedComment;
import io.github.rigsto.emailvalidator.warning.TLD;

import java.net.IDN;
import java.util.HashMap;
import java.util.Map;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;

/**
 * Parser for the domain part of email addresses.
 * <p>
 * This parser handles the validation of domain parts in email addresses,
 * including domain names, domain literals, and various domain-related
 * syntax rules according to RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class DomainPart extends PartParser {
    /**
     * Maximum allowed length for domain names (253 characters).
     */
    public static int DOMAIN_MAX_LENGTH = 253;
    
    /**
     * Maximum allowed length for domain labels (63 characters).
     */
    public static int LABEL_MAX_LENGTH = 63;

    /**
     * The parsed domain part string.
     */
    private String domainPart = "";
    
    /**
     * The current domain label being processed.
     */
    private String label = "";

    /**
     * Creates a new DomainPart parser with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public DomainPart(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses the domain part of an email address.
     * <p>
     * Validates domain syntax, checks for domain literals, handles comments,
     * and ensures compliance with RFC standards for domain parts.
     * </p>
     * 
     * @return ValidEmail if the domain part is valid, InvalidEmail otherwise
     */
    @Override
    public Result parse() {
        this.lexer.clearRecorded();
        this.lexer.startRecording();

        this.lexer.moveNext();

        Result domainStartChecks = performDomainStartChecks();
        if (domainStartChecks.isInvalid()) {
            return domainStartChecks;
        }

        if (this.lexer.current.isA(S_AT)) {
            return new InvalidEmail(new ConsecutiveAt(), this.lexer.current.value);
        }

        Result r = doParseDomainPart();
        if (r.isInvalid()) {
            return r;
        }

        Result end = checkEndOfDomain();
        if (end.isInvalid()) {
            return end;
        }

        this.lexer.stopRecording();
        this.domainPart = this.lexer.getAccumulatedValues();

        int length = this.domainPart.length();
        if (length > DOMAIN_MAX_LENGTH) {
            return new InvalidEmail(new DomainTooLong(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    private Result checkEndOfDomain() {
        Token<Integer, String> prev = this.lexer.getPrevious();

        if (prev.isA(S_DOT)) {
            return new InvalidEmail(new DotAtEnd(), this.lexer.current.value);
        }

        if (prev.isA(S_HYPHEN)) {
            return new InvalidEmail(new DomainHyphened("Hypen found at the end of the domain"), prev.value);
        }

        if (this.lexer.current.isA(S_SP)) {
            return new InvalidEmail(new CRLFAtTheEnd(), prev.value);
        }

        return new ValidEmail();
    }

    private Result performDomainStartChecks() {
        Result invalidAfterAt = checkInvalidTokensAfterAT();
        if (invalidAfterAt.isInvalid()) {
            return invalidAfterAt;
        }

        Result missingDomain = checkEmptyDomain();
        if (missingDomain.isInvalid()) {
            return missingDomain;
        }

        if (lexer.current.isA(S_OPENPARENTHESIS)) {
            this.warnings.add(new DeprecatedComment());
        }

        return new ValidEmail();
    }

    private Result checkEmptyDomain() {
        boolean thereIsNoDomain = this.lexer.current.isA(S_EMPTY) ||
                (this.lexer.current.isA(S_SP) && !this.lexer.isNextToken(GENERIC));

        if (thereIsNoDomain) {
            return new InvalidEmail(new NoDomainPart(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    private Result checkInvalidTokensAfterAT() {
        if (this.lexer.current.isA(S_DOT)) {
            return new InvalidEmail(new DotAtStart(), this.lexer.current.value);
        }

        if (this.lexer.current.isA(S_HYPHEN)) {
            return new InvalidEmail(new DomainHyphened("After AT"), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    protected Result parseComments() {
        Comment commentParser = new Comment(this.lexer, new DomainComment());
        Result res = commentParser.parse();

        this.warnings.addAll(commentParser.getWarnings());
        return res;
    }

    protected Result doParseDomainPart() {
        boolean tldMissing = true;
        boolean hasComments = false;
        StringBuilder domain = new StringBuilder();

        do {
            Token<Integer, String> prev = this.lexer.getPrevious();

            Result notAllowed = checkNotAllowedChars(this.lexer.current);
            if (notAllowed.isInvalid()) {
                return notAllowed;
            }

            if (this.lexer.current.isA(S_OPENPARENTHESIS) || this.lexer.current.isA(S_CLOSEPARENTHESIS)) {
                hasComments = true;
                Result commentsResult = parseComments();

                if (commentsResult.isInvalid()) {
                    return commentsResult;
                }
            }

            Result dots = checkConsecutiveDots();
            if (dots.isInvalid()) {
                return dots;
            }

            if (this.lexer.current.isA(S_OPENBRACKET)) {
                Result literalResult = parseDomainLiteral();
                addTLDWarnings(tldMissing);
                return literalResult;
            }

            Result labelCheck = checkLabelLength();
            if (labelCheck.isInvalid()) {
                return labelCheck;
            }

            Result fws = parseFWS();
            if (fws.isInvalid()) {
                return fws;
            }

            domain.append(this.lexer.current.value);

            if (this.lexer.current.isA(S_DOT) && this.lexer.isNextToken(GENERIC)) {
                tldMissing = false;
            }

            Result ex = checkDomainPartExceptions(prev, hasComments);
            if (ex.isInvalid()) {
                return ex;
            }

            lexer.moveNext();
        } while (!this.lexer.current.isA(S_EMPTY));

        Result labelEnd = checkLabelLength(true);
        if (labelEnd.isInvalid()) {
            return labelEnd;
        }

        addTLDWarnings(tldMissing);

        this.domainPart = domain.toString();
        return new ValidEmail();
    }

    private Result checkNotAllowedChars(Token<Integer, String> token) {
        Map<Integer, Boolean> notAllowed = new HashMap<>();
        notAllowed.put(S_BACKSLASH, true);
        notAllowed.put(S_SLASH, true);

        if (notAllowed.containsKey(token.type)) {
            return new InvalidEmail(new CharNotAllowed(), token.value);
        }
        return new ValidEmail();
    }

    protected Result parseDomainLiteral() {
        try {
            this.lexer.find(S_CLOSEBRACKET);
        } catch (RuntimeException e) {
            return new InvalidEmail(new ExpectingDomainLiteralClose(), this.lexer.current.value);
        }

        DomainLiteral domainLiteralParser = new DomainLiteral(this.lexer);
        Result res = domainLiteralParser.parse();

        this.warnings.addAll(domainLiteralParser.getWarnings());
        return res;
    }

    protected Result checkDomainPartExceptions(Token<Integer, String> prev, boolean hasComments) {
        if (this.lexer.current.isA(S_OPENBRACKET) && prev.type != S_AT) {
            return new InvalidEmail(new ExpectingATEXT("OPENBRACKET not after AT"), this.lexer.current.value);
        }

        if (this.lexer.current.isA(S_HYPHEN) && this.lexer.isNextToken(S_DOT)) {
            return new InvalidEmail(new DomainHyphened("Hypen found near DOT"), this.lexer.current.value);
        }

        if (this.lexer.current.isA(S_BACKSLASH) && this.lexer.isNextToken(GENERIC)) {
            return new InvalidEmail(new ExpectingATEXT("Escaping following 'ATOM'"), this.lexer.current.value);
        }

        return validateTokens(hasComments);
    }

    protected Result validateTokens(boolean hasComments) {
        Map<Integer, Boolean> validDomainTokens = new HashMap<>();
        validDomainTokens.put(GENERIC, true);
        validDomainTokens.put(S_HYPHEN, true);
        validDomainTokens.put(S_DOT, true);

        if (hasComments) {
            validDomainTokens.put(S_OPENPARENTHESIS, true);
            validDomainTokens.put(S_CLOSEPARENTHESIS, true);
        }

        if (!validDomainTokens.containsKey(this.lexer.current.type)) {
            return new InvalidEmail(
                    new ExpectingATEXT("Invalid token in domain: " + this.lexer.current.value),
                    this.lexer.current.value
            );
        }

        return new ValidEmail();
    }

    private Result checkLabelLength() {
        return checkLabelLength(false);
    }

    private Result checkLabelLength(boolean isEndOfDomain) {
        if (this.lexer.current.isA(S_DOT) || isEndOfDomain) {
            if (isLabelTooLong(this.label)) {
                return new InvalidEmail(new LabelTooLong(), this.lexer.current.value);
            }
            this.label = "";
        }

        this.label += this.lexer.current.value;
        return new ValidEmail();
    }

    private boolean isLabelTooLong(String label) {
        if (label.codePoints().anyMatch(cp -> cp > 0x7F)) {
            try {
                String ascii = IDN.toASCII(label, IDN.USE_STD3_ASCII_RULES);
                return ascii.length() > LABEL_MAX_LENGTH;
            } catch (IllegalArgumentException e) {
                return true;
            }
        }

        return label.length() > LABEL_MAX_LENGTH;
    }

    private void addTLDWarnings(boolean isTLDMissing) {
        if (isTLDMissing) {
            this.warnings.add(new TLD());
        }
    }

    /**
     * Returns the parsed domain part string.
     * 
     * @return the domain part string
     */
    public String domainPart() {
        return this.domainPart;
    }
}
