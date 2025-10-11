package com.xtmd.emailvalidator;

import com.xtmd.emailvalidator.constant.Constant;
import com.xtmd.emailvalidator.parser.IDLeftPart;
import com.xtmd.emailvalidator.parser.IDRightPart;
import com.xtmd.emailvalidator.result.InvalidEmail;
import com.xtmd.emailvalidator.result.Result;
import com.xtmd.emailvalidator.result.ValidEmail;
import com.xtmd.emailvalidator.result.reason.NoLocalPart;
import com.xtmd.emailvalidator.warning.EmailTooLong;

public class MessageIDParser extends Parser {

    protected String idLeft = "";
    protected String idRight = "";

    public MessageIDParser(EmailLexer lexer) {
        super(lexer);
    }

    @Override
    public Result parse(String str) {
        Result result = super.parse(str);
        addLongEmailWarning(this.idLeft, this.idRight);
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
        return processIDLeft();
    }

    @Override
    protected Result parseRightFromAt() {
        return processIDRight();
    }

    private Result processIDLeft() {
        IDLeftPart leftParser = new IDLeftPart(this.lexer);
        Result result = leftParser.parse();

        this.idLeft = leftParser.localPart();
        this.warnings.addAll(leftParser.getWarnings());

        return result;
    }

    private Result processIDRight() {
        IDRightPart rightParser = new IDRightPart(this.lexer);
        Result result = rightParser.parse();

        this.idRight = rightParser.domainPart();
        this.warnings.addAll(rightParser.getWarnings());

        return result;
    }

    public String getLeftPart() {
        return this.idLeft;
    }

    public String getRightPart() {
        return this.idRight;
    }

    private void addLongEmailWarning(String localPart, String parsedDomainPart) {
        String text = localPart + "@" + parsedDomainPart;

        if (text.length() > Constant.EMAIL_ID_MAX_LENGTH) {
            this.warnings.add(new EmailTooLong());
        }
    }
}
