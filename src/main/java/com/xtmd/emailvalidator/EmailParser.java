package com.xtmd.emailvalidator;

import com.xtmd.emailvalidator.constant.Constant;
import com.xtmd.emailvalidator.parser.DomainPart;
import com.xtmd.emailvalidator.parser.LocalPart;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.NoLocalPart;
import com.xtmd.emailvalidator.warning.EmailTooLong;

public class EmailParser extends Parser {

    protected String domainPart = "";
    protected String localPart = "";

    public EmailParser(EmailLexer lexer) {
        super(lexer);
    }

    public Result parse(String str) {
        Result result = super.parse(str);
        addLongEmailWarning(this.localPart, this.domainPart);
        return result;
    }

    @Override
    protected Result preLeftParsing() {
        if (!hasAtToken()) {
            return new InvalidEmail(new NoLocalPart(), this.lexer.current.value);
        }

        return new ValidEmail();
    }

    @Override
    protected Result parseLeftFromAt() {
        return processLocalPart();
    }

    @Override
    protected Result parseRightFromAt() {
        return processDomainPart();
    }

    private Result processLocalPart() {
        LocalPart localParser = new LocalPart(this.lexer);
        Result result = localParser.parse();

        this.localPart = localParser.localPart();
        this.warnings.addAll(localParser.getWarnings());

        return result;
    }

    private Result processDomainPart() {
        DomainPart domainParser = new DomainPart(this.lexer);
        Result result = domainParser.parse();

        this.domainPart = domainParser.domainPart();
        this.warnings.addAll(domainParser.getWarnings());

        return result;
    }

    public String getDomainPart() {
        return this.domainPart;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    private void addLongEmailWarning(String localPart, String parsedDomainPart) {
        String text = localPart + "@" + parsedDomainPart;

        if (text.length() > Constant.EMAIL_MAX_LENGTH) {
            this.warnings.add(new EmailTooLong());
        }
    }
}
