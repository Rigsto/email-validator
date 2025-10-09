package com.xtmd.emailvalidator.parser;

import com.xtmd.emailvalidator.lexer.EmailLexer;
import com.xtmd.emailvalidator.warning.Warning;

import java.util.ArrayList;
import java.util.List;

abstract class PartParser {
    protected List<Warning> warnings = new ArrayList<>();
    protected EmailLexer lexer;

    public PartParser(EmailLexer lexer) {
        this.lexer = lexer;
    }
}
