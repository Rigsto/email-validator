package com.xtmd.emailvalidator.result;

import com.xtmd.emailvalidator.result.reason.CharNotAllowed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    @Test
    void testResultIsValidEmail() {
        ValidEmail result = new ValidEmail();

        int expectedCode = 0;
        String expectedDescription = "Valid email";

        assertTrue(result.isValid());
        assertEquals(expectedCode, result.code());
        assertEquals(expectedDescription, result.description());
    }

    @Test
    void testResultIsInvalidEmail() {
        CharNotAllowed reason = new CharNotAllowed();
        String token = "T";
        InvalidEmail result = new InvalidEmail(reason, token);

        int expectedCode = reason.code();
        String expectedDescription = reason.description() + " in char " + token;

        assertFalse(result.isValid());
        assertEquals(expectedCode, result.code());
        assertEquals(expectedDescription, result.description());
    }
}
