package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.constant.Constant;
import io.github.rigsto.emailvalidator.parser.IDLeftPart;
import io.github.rigsto.emailvalidator.parser.IDRightPart;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.NoLocalPart;
import io.github.rigsto.emailvalidator.warning.EmailTooLong;

/**
 * Parser for Message-ID headers in email messages.
 * <p>
 * This class extends the base Parser to provide specific parsing logic for
 * Message-ID headers. It handles the parsing of ID left parts and ID right
 * parts, and includes validation for Message-ID length limits.
 * </p>
 * <p>
 * The parser uses specialized IDLeftPart and IDRightPart parsers to handle
 * the specific syntax rules for Message-ID headers as defined in RFC standards.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class MessageIDParser extends Parser {

    /**
     * The parsed left part of the Message-ID.
     */
    protected String idLeft = "";
    
    /**
     * The parsed right part of the Message-ID.
     */
    protected String idRight = "";

    /**
     * Creates a new MessageIDParser instance with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public MessageIDParser(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses the input string as a Message-ID.
     * <p>
     * Extends the base parsing to include Message-ID length validation
     * after successful parsing.
     * </p>
     * 
     * @param str the Message-ID string to parse
     * @return the parsing result
     */
    @Override
    public Result parse(String str) {
        Result result = super.parse(str);
        addLongEmailWarning(this.idLeft, this.idRight);
        return result;
    }

    /**
     * Performs pre-parsing validation for Message-IDs.
     * <p>
     * Checks if the Message-ID contains an @ symbol, which is required
     * for valid Message-ID headers.
     * </p>
     * 
     * @return the pre-parsing result
     */
    @Override
    protected Result preLeftParsing() {
        if (!hasAtToken()) {
            return new InvalidEmail(new NoLocalPart(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    /**
     * Parses the left part of the Message-ID.
     * 
     * @return the parsing result for the left part
     */
    @Override
    protected Result parseLeftFromAt() {
        return processIDLeft();
    }

    /**
     * Parses the right part of the Message-ID.
     * 
     * @return the parsing result for the right part
     */
    @Override
    protected Result parseRightFromAt() {
        return processIDRight();
    }

    /**
     * Processes the left part of the Message-ID.
     * <p>
     * Uses an IDLeftPart parser to handle the specific syntax rules
     * for the left part and collects any warnings generated.
     * </p>
     * 
     * @return the parsing result for the left part
     */
    private Result processIDLeft() {
        IDLeftPart leftParser = new IDLeftPart(this.lexer);
        Result result = leftParser.parse();

        this.idLeft = leftParser.localPart();
        this.warnings.addAll(leftParser.getWarnings());

        return result;
    }

    /**
     * Processes the right part of the Message-ID.
     * <p>
     * Uses an IDRightPart parser to handle the specific syntax rules
     * for the right part and collects any warnings generated.
     * </p>
     * 
     * @return the parsing result for the right part
     */
    private Result processIDRight() {
        IDRightPart rightParser = new IDRightPart(this.lexer);
        Result result = rightParser.parse();

        this.idRight = rightParser.domainPart();
        this.warnings.addAll(rightParser.getWarnings());

        return result;
    }

    /**
     * Returns the parsed left part of the Message-ID.
     * 
     * @return the left part string
     */
    public String getLeftPart() {
        return this.idLeft;
    }

    /**
     * Returns the parsed right part of the Message-ID.
     * 
     * @return the right part string
     */
    public String getRightPart() {
        return this.idRight;
    }

    /**
     * Adds a warning if the Message-ID exceeds the maximum allowed length.
     * <p>
     * Checks the combined length of left part and right part against
     * the maximum Message-ID length constant.
     * </p>
     * 
     * @param localPart the left part of the Message-ID
     * @param parsedDomainPart the right part of the Message-ID
     */
    private void addLongEmailWarning(String localPart, String parsedDomainPart) {
        String text = localPart + "@" + parsedDomainPart;

        if (text.length() > Constant.EMAIL_ID_MAX_LENGTH) {
            this.warnings.add(new EmailTooLong());
        }
    }
}
