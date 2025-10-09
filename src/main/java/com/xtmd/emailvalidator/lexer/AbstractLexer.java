package com.xtmd.emailvalidator.lexer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractLexer<T, V> {
    private String input = "";
    private final List<Token<T, V>> tokens = new ArrayList<>();
    private int position = 0;
    private int peek = 0;

    public Token<T, V> lookahead;
    public Token<T, V> token;
    public Pattern regex;

    public void setInput(String input) {
        this.input = (input != null)  ? input : "";
        this.tokens.clear();

        reset();
        scan(this.input);
    }

    public void reset() {
        this.lookahead = null;
        this.token = null;
        this.peek = 0;
        this.position = 0;
    }

    public void resetPeek() {
        this.peek = 0;
    }

    public void resetPosition() {
        this.position = Math.max(0, position);
    }

    public String getInputUntilPosition(int position) {
        int end = Math.max(0, Math.min(position, input.length()));
        return input.substring(0, end);
    }

    public boolean isNextToken(T type) {
        return this.lookahead != null && this.lookahead.isA(type);
    }

    public boolean isNextTokenAny(List<T> types) {
        if (this.lookahead == null || types == null || types.isEmpty()) {
            return false;
        }

        for (T type : types) {
            if (this.lookahead.isA(type)) {
                return true;
            }
        }

        return false;
    }

    public boolean moveNext() {
        this.peek = 0;
        this.token = this.lookahead;
        this.lookahead = (this.position < this.tokens.size())
                ? this.tokens.get(this.position++)
                : null;

        return this.lookahead != null;
    }

    public void skipUntil(T type) {
        while (this.lookahead != null && !this.lookahead.isA(type)) {
            moveNext();
        }
    }

    public boolean isA(String value, T tokenType) {
        T derived = getType(value);
        return Objects.equals(derived, tokenType);
    }

    public Token<T, V> peek() {
        int idx = this.position + this.peek;

        if (idx >= 0 && idx < this.tokens.size()) {
            this.peek++;
            return this.tokens.get(idx);
        }

        return null;
    }

    public Token<T, V> glimpse() {
        Token<T, V> p = peek();
        this.peek = 0;
        return p;
    }

    protected void scan(String input) {
        if (this.regex == null) {
            String[] catchables = getCatchablePatterns();
            String[] nonCatchables = getNonCatchablePatterns();

            StringBuilder sb = new StringBuilder();

            if (catchables != null && catchables.length > 0) {
                sb.append("(")
                        .append(String.join(")|(", catchables))
                        .append(")");

                if (nonCatchables != null && nonCatchables.length > 0) {
                    sb.append("|");
                }
            }

            if (nonCatchables != null && nonCatchables.length > 0) {
                sb.append(String.join("|", nonCatchables));
            }

            int flags = toPatternFlags(getModifiers());
            this.regex = Pattern.compile(sb.toString(), flags);
        }

        Matcher matcher = this.regex.matcher(input);
        while (matcher.find()) {
            String firstMatch = matcher.group();
            int offset = matcher.start();

            T type = getType(firstMatch);
            V value = transformValue(firstMatch, type);

            this.tokens.add(new Token<>(value, type, offset));
        }
    }

    public String getLiteral(T tokenType) {
        if (tokenType == null) return "null";

        if (tokenType instanceof Enum<?> e) {
            return e.getClass().getName() + "::" + e.name();
        }

        Class<?> clazz = this.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            int mods = f.getModifiers();

            if (!Modifier.isStatic(mods) || !Modifier.isFinal(mods) || !Modifier.isPublic(mods)) {
                continue;
            }

            try {
                Object val = f.get(null);
                if (Objects.equals(val, tokenType)) {
                    return clazz.getName() + "::" + f.getName();
                }
            } catch (IllegalAccessException ignored) {}
        }

        return tokenType.toString();
    }

    protected String getModifiers() {
        return "iu";
    }

    private static int toPatternFlags(String modifiers) {
        int flags = 0;

        if (modifiers == null) {
            return flags;
        }

        for (char c : modifiers.toCharArray()) {
            switch (c) {
                case 'i' -> {
                    flags |= Pattern.CASE_INSENSITIVE;
                    flags |= Pattern.UNICODE_CASE;
                }
                case 'u' -> {
                    flags |= Pattern.UNICODE_CHARACTER_CLASS;
                }
                case 'm' -> flags |= Pattern.MULTILINE;
                case 's' -> flags |= Pattern.DOTALL;
                default -> {}
            }
        }

        return flags;
    }

    protected abstract String[] getCatchablePatterns();
    protected abstract String[] getNonCatchablePatterns();
    protected abstract T getType(String value);

    @SuppressWarnings("unchecked")
    protected V transformValue(String raw, T type) {
        return (V) raw;
    }
}
