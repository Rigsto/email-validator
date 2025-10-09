package com.xtmd.emailvalidator.warning;

import com.xtmd.emailvalidator.constant.Constant;

public class EmailTooLong extends Warning {
    EmailTooLong() {
        super(66, "Email too long, exceeds " + Constant.EMAIL_MAX_LENGTH, 0);
    }
}
