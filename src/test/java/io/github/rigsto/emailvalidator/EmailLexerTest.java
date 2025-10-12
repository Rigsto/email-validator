package io.github.rigsto.emailvalidator;

import io.github.rigsto.emailvalidator.lexer.AbstractLexer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.github.rigsto.emailvalidator.constant.LexerConstant.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmailLexerTest {

    @Test
    void testLexerExtendsAbstract() {
        EmailLexer lexer = new EmailLexer();
        assertInstanceOf(AbstractLexer.class, lexer);
    }

    @ParameterizedTest
    @MethodSource("getTokens")
    void testLexerTokens(String str, int token) {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput(str);
        lexer.moveNext();
        lexer.moveNext();
        assertEquals(token, lexer.current.type);
    }

    @Test
    void testLexerParsesMultipleSpaces() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("  ");
        lexer.moveNext();
        lexer.moveNext();
        assertEquals(S_SP, lexer.current.type);

        lexer.moveNext();
        assertEquals(S_SP, lexer.current.type);
    }

    @ParameterizedTest
    @MethodSource("invalidUTF8CharsProvider")
    void testLexerParsesInvalidUTF8(String ch) {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput(ch);
        lexer.moveNext();
        lexer.moveNext();
        assertEquals(INVALID, lexer.current.type);
    }

    @Test
    void testLexerForTab() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("foo\tbar");
        lexer.moveNext();
        lexer.skipUntil(S_HTAB);
        lexer.moveNext();
        assertEquals(S_HTAB, lexer.current.type);
    }

    @Test
    void testLexerForUTF8() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("áÇ@bar.com");
        lexer.moveNext();
        lexer.moveNext();
        assertEquals(GENERIC, lexer.current.type);
        
        lexer.moveNext();
        assertEquals(GENERIC, lexer.current.type);
    }
    
    @Test
    void testLexerSearchToken() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("foo\tbar");
        lexer.moveNext();
        assertTrue(lexer.find(S_HTAB));
    }
    
    @Test
    void testRecordIsOffAtStart() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("foo-bar");
        lexer.moveNext();
        assertEquals("", lexer.getAccumulatedValues());
    }
    
    @Test
    void testRecord() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("foo-bar");
        lexer.startRecording();
        lexer.moveNext();
        lexer.moveNext();
        assertEquals("foo",  lexer.getAccumulatedValues());
    }
    
    @Test
    void testRecordAndClear() {
        EmailLexer lexer = new EmailLexer();
        lexer.setInput("foo-bar");
        lexer.startRecording();
        lexer.moveNext();
        lexer.moveNext();
        lexer.clearRecorded();
        assertEquals("", lexer.getAccumulatedValues());
    }

    static Stream<String> invalidUTF8CharsProvider() {
        List<String> out = new ArrayList<>();
        for (int cp = 0; cp < 0x100; cp++) {
            if (cp == 0x00 || cp == 0x09 || cp == 0x0A || cp == 0x0D) continue;
            if (Character.getType(cp) == Character.CONTROL) {
                out.add(new String(Character.toChars(cp)));
            }
        }
        return out.stream();
    }

    static Stream<Arguments> getTokens() {
        return Stream.of(
                Arguments.of("foo", GENERIC),
                Arguments.of("\r", S_CR),
                Arguments.of("\t", S_HTAB),
                Arguments.of("\r\n", CRLF),
                Arguments.of("\n", S_LF),
                Arguments.of(" ", S_SP),
                Arguments.of("@", S_AT),
                Arguments.of("IPv6", S_IPV6TAG),
                Arguments.of("::", S_DOUBLECOLON),
                Arguments.of(":", S_COLON),
                Arguments.of(".", S_DOT),
                Arguments.of("\"", S_DQUOTE),
                Arguments.of("`", S_BACKTICK),
                Arguments.of("'", S_SQUOTE),
                Arguments.of("-", S_HYPHEN),
                Arguments.of("\\", S_BACKSLASH),
                Arguments.of("/", S_SLASH),
                Arguments.of("(", S_OPENPARENTHESIS),
                Arguments.of(")", S_CLOSEPARENTHESIS),
                Arguments.of("<", S_LOWERTHAN),
                Arguments.of(">", S_GREATERTHAN),
                Arguments.of("[", S_OPENBRACKET),
                Arguments.of("]", S_CLOSEBRACKET),
                Arguments.of(";", S_SEMICOLON),
                Arguments.of(",", S_COMMA),
                Arguments.of("{", S_OPENCURLYBRACES),
                Arguments.of("}", S_CLOSECURLYBRACES),
                Arguments.of("|", S_PIPE),
                Arguments.of("~", S_TILDE),
                Arguments.of("=", S_EQUAL),
                Arguments.of("+", S_PLUS),
                Arguments.of("¿", INVERT_QUESTIONMARK),
                Arguments.of("?", QUESTIONMARK),
                Arguments.of("#", NUMBER_SIGN),
                Arguments.of("¡", INVERT_EXCLAMATION),
                Arguments.of("_", S_UNDERSCORE),
                Arguments.of("",  S_EMPTY),
                Arguments.of(String.valueOf((char)31),  INVALID),
                Arguments.of(String.valueOf((char)226), GENERIC),
                Arguments.of(String.valueOf((char)0),   C_NUL)
        );
    }
}
