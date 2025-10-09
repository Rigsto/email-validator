package com.xtmd.emailvalidator.lexer;

import java.util.Objects;

public final class Token<T, V> {
    public final V value;
    public final T type;
    public final int position;

    public Token(V value, T type, int position) {
        this.value = value;
        this.type = type;
        this.position = position;
    }

    @SafeVarargs
    public final boolean isA(T... types) {
        if (types == null) return false;

        for (T type : types) {
            if (Objects.equals(this.type, type)) return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Token{value=" + value + ", type=" + type + ", position=" + position + '}';
    }
}
