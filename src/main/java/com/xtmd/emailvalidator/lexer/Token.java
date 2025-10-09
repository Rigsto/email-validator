package com.xtmd.emailvalidator.lexer;

final public class Token implements TokenInterface {
    private String name;
    private String text;

    public Token(String name, String text) {
        this.name = name;
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TokenInterface)) return false;
        TokenInterface token = (TokenInterface) obj;

        return this.name.equals(token.getName());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
