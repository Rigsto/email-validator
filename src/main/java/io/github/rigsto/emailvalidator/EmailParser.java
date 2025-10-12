package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.constant.Constant;
import io.github.rigsto.emailvalidator.parser.DomainPart;
import io.github.rigsto.emailvalidator.parser.LocalPart;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.Result;
import io.github.rigsto.emailvalidator.result.ValidEmail;
import io.github.rigsto.emailvalidator.result.reason.NoLocalPart;
import io.github.rigsto.emailvalidator.warning.EmailTooLong;

/**
 * Parser for standard email addresses.
 * <p>
 * This class extends the base Parser to provide specific parsing logic for
 * standard email addresses. It handles the parsing of local parts and domain
 * parts, and includes validation for email length limits.
 * </p>
 * <p>
 * The parser uses specialized LocalPart and DomainPart parsers to handle
 * the specific syntax rules for each part of an email address.
 * </p>
 * 
 * @author EmailValidator Team
 * @since 0.0.1
 */
public class EmailParser extends Parser {

    /**
     * The parsed domain part of the email address.
     */
    protected String domainPart = "";
    
    /**
     * The parsed local part of the email address.
     */
    protected String localPart = "";

    /**
     * Creates a new EmailParser instance with the specified lexer.
     * 
     * @param lexer the lexer to use for tokenization
     */
    public EmailParser(EmailLexer lexer) {
        super(lexer);
    }

    /**
     * Parses the input string as an email address.
     * <p>
     * Extends the base parsing to include email length validation
     * after successful parsing.
     * </p>
     * 
     * @param str the email address string to parse
     * @return the parsing result
     */
    public Result parse(String str) {
        Result result = super.parse(str);
        addLongEmailWarning(this.localPart, this.domainPart);
        return result;
    }

    /**
     * Performs pre-parsing validation for email addresses.
     * <p>
     * Checks if the email contains an @ symbol, which is required
     * for valid email addresses.
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
     * Parses the left part (local part) of the email address.
     * 
     * @return the parsing result for the local part
     */
    @Override
    protected Result parseLeftFromAt() {
        return processLocalPart();
    }

    /**
     * Parses the right part (domain part) of the email address.
     * 
     * @return the parsing result for the domain part
     */
    @Override
    protected Result parseRightFromAt() {
        return processDomainPart();
    }

    /**
     * Processes the local part of the email address.
     * <p>
     * Uses a LocalPart parser to handle the specific syntax rules
     * for the local part and collects any warnings generated.
     * </p>
     * 
     * @return the parsing result for the local part
     */
    private Result processLocalPart() {
        LocalPart localParser = new LocalPart(this.lexer);
        Result result = localParser.parse();

        this.localPart = localParser.localPart();
        this.warnings.addAll(localParser.getWarnings());

        return result;
    }

    /**
     * Processes the domain part of the email address.
     * <p>
     * Uses a DomainPart parser to handle the specific syntax rules
     * for the domain part and collects any warnings generated.
     * </p>
     * 
     * @return the parsing result for the domain part
     */
    private Result processDomainPart() {
        DomainPart domainParser = new DomainPart(this.lexer);
        Result result = domainParser.parse();

        this.domainPart = domainParser.domainPart();
        this.warnings.addAll(domainParser.getWarnings());

        return result;
    }

    /**
     * Returns the parsed domain part of the email address.
     * 
     * @return the domain part string
     */
    public String getDomainPart() {
        return this.domainPart;
    }

    /**
     * Returns the parsed local part of the email address.
     * 
     * @return the local part string
     */
    public String getLocalPart() {
        return this.localPart;
    }

    /**
     * Adds a warning if the email address exceeds the maximum allowed length.
     * <p>
     * Checks the combined length of local part and domain part against
     * the maximum email length constant.
     * </p>
     * 
     * @param localPart the local part of the email
     * @param parsedDomainPart the domain part of the email
     */
    private void addLongEmailWarning(String localPart, String parsedDomainPart) {
        String text = localPart + "@" + parsedDomainPart;

        if (text.length() > Constant.EMAIL_MAX_LENGTH) {
            this.warnings.add(new EmailTooLong());
        }
    }
}
