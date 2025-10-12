package io.github.rigsto.emailvalidator.warning;

import io.github.rigsto.emailvalidator.constant.Constant;

public class EmailTooLong extends Warning {
    public static final int CODE = 66;

    public EmailTooLong() {
        this.message = "Email too long, exceeds " + Constant.EMAIL_MAX_LENGTH;
    }

}
