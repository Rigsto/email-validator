package io.github.rigsto.emailvalidator.validation.extra;

import io.github.rigsto.emailvalidator.EmailLexer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpoofCheckValidationTest {

    @ParameterizedTest
    @MethodSource("validUTF8EmailsProvider")
    void testUTF8EmailAreValid(String email) {
        SpoofCheckValidation validation = new SpoofCheckValidation();
        assertTrue(validation.isValid(email, new EmailLexer()));
    }

    @Test
    void testEmailWithSpoofIsInvalid() {
        SpoofCheckValidation validation = new SpoofCheckValidation();
        String spoof = "Кириллица" + "latin漢字" + "ひらがな" + "カタカナ";
        assertFalse(validation.isValid(spoof, new EmailLexer()));
    }

    static Stream<String> validUTF8EmailsProvider() {
        return Stream.of(
                // Cyrillic
                "Кириллица@Кириллица",
                // Latin + Han + Hiragana + Katakana
                "latin漢字" + "ひらがな" + "カタカナ" + "@example.com",
                // Latin + Han + Hangul
                "latin" + "漢字" + "조선말" + "@example.com",
                // Latin + Han + Bopomofo
                "latin" + "漢字" + "ㄅㄆㄇㄈ" + "@example.com"
        );
    }
}
