package io.github.rigsto.emailvalidator.validation;

import io.github.rigsto.emailvalidator.EmailLexer;
import io.github.rigsto.emailvalidator.result.InvalidEmail;
import io.github.rigsto.emailvalidator.result.reason.NoDomainPart;
import io.github.rigsto.emailvalidator.result.reason.RFCWarnings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NoRFCWarningsValidationTest {

    @Test
    void testInvalidEmailIsInvalid() {
        NoRFCWarningsValidation v = new NoRFCWarningsValidation();
        assertFalse(v.isValid("non-email-string", new EmailLexer()));

        InvalidEmail err = v.getError();
        assertNotNull(err);
        assertInstanceOf(NoDomainPart.class, err.getReason());
    }

    @Test
    void testEmailWithWarningsIsInvalid() {
        NoRFCWarningsValidation v = new NoRFCWarningsValidation();
        assertFalse(v.isValid("test()@example.com", new EmailLexer()));

        InvalidEmail err = v.getError();
        assertNotNull(err);
        assertInstanceOf(RFCWarnings.class, err.getReason());
    }

    @ParameterizedTest
    @MethodSource("validEmailsWithoutWarnings")
    void testEmailWithoutWarningIsValid(String email) {
        NoRFCWarningsValidation v = new NoRFCWarningsValidation();

        assertTrue(v.isValid("example@example.com", new EmailLexer()));
        assertTrue(v.isValid(email, new EmailLexer()));
        assertNull(v.getError());
    }

    static Stream<String> validEmailsWithoutWarnings() {
        String fortyCyrillic = "ъ".repeat(40);
        return Stream.of(
                "example@example.com",
                String.format("example@%s.com", fortyCyrillic)
        );
    }
}
