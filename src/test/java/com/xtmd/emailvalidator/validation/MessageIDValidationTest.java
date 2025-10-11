package com.xtmd.emailvalidator.validation;

import com.xtmd.emailvalidator.EmailLexer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageIDValidationTest {

    @ParameterizedTest
    @MethodSource("validMessageIDs")
    void testValidMessageID(String messageID) {
        MessageIDValidation validator = new MessageIDValidation();
        assertTrue(validator.isValid(messageID, new EmailLexer()), "Should be valid: " + messageID);
    }

    @ParameterizedTest
    @MethodSource("invalidMessageIDs")
    void testInvalidMessageID(String messageID) {
        MessageIDValidation validator = new MessageIDValidation();
        assertFalse(validator.isValid(messageID, new EmailLexer()), "Should be invalid: " + messageID);
    }

    static Stream<String> validMessageIDs() {
        return Stream.of(
                "a@b.c+&%$.d",
                "a.b+&%$.c@d",
                "a@ä"
        );
    }

    static Stream<String> invalidMessageIDs() {
        return Stream.of(
                "example",
                "example@with space",
                "example@iana.",
                "example@ia\na.",               // contains a LF in the domain
                "example(comment)@example.com", // comments not allowed in msg-id
                "\r\nFWS@example.com"           // starts with CRLF (folding whitespace)
        );
    }
}
